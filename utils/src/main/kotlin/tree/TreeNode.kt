
package tree

class TreeNode<T>(value: T) {
    val size: Int get() = root.size
    private val root: Node<T> = RootNode(value)

    fun first(): Node<T> = root
    fun add(value: T, node: Node<T>): Node<T> {
        return node.add(value)
    }

    fun forEach(
        type: TreeVisitorType = TreeVisitorType.DEPTH_FIRST,
        maxDepth: Int,
        visitor: (Node<T>) -> Unit
    ) {
        forEachIndexed(type, maxDepth) { _, node -> visitor(node) }
    }

    fun forEachIndexed(
        type: TreeVisitorType = TreeVisitorType.DEPTH_FIRST,
        maxDepth: Int,
        visitor: (Int, Node<T>) -> Unit
    ) {
        val start = root as? RootNode<T> ?: return
        when (type) {
            TreeVisitorType.DEPTH_FIRST -> RootNode.depthFirstIterator(start, maxDepth, visitor)
            TreeVisitorType.BREATH_FIRST -> RootNode.breathFirstIterator(start, maxDepth, visitor)
        }
    }

    interface Node<T> {
        val size: Int
        val value: T
        val depth: Int
        val isRoot: Boolean
        val parent: Node<T>?

        fun add(value: T): Node<T>
        fun children(visitor: (Node<T>) -> Unit) {
            children { _, node -> visitor(node) }
        }
        fun children(visitor: (Int, Node<T>) -> Unit)
    }

    private interface Container<T>: Node<T> {
        val container: MutableList<Container<T>>

        fun added(childNode: Node<T>)
    }

    private interface ChildNodeProvider<T> {
        fun get(index: Int, depth: Int): Node<T>?
        fun firstIndexOf(depth: Int, matching: (Node<T>) -> Boolean): Int
        fun getAll(depth: Int, matching: (Node<T>) -> Boolean, visitor: (Node<T>) -> Unit)
    }

    private class ChildNode<T>(
        override val value: T,
        override val depth: Int,
        override val parent: Container<T>? = null,
        val provider: ChildNodeProvider<T>
    ): Container<T> {
        override val size: Int get() {
            var count = 1
            children { node -> count += node.size }
            return count
        }
        override val isRoot: Boolean = false
        override val container: MutableList<Container<T>> = mutableListOf()

        override fun add(value: T): Node<T> {
            return ChildNode(value, depth + 1, this, provider).also {
                added(it)
                container.add(it)
            }
        }
        override fun added(childNode: Node<T>) {
            parent?.added(childNode)
        }

        override fun children(visitor: (Int, Node<T>) -> Unit) {
//            var idx = 0
//            provider.getAll(depth + 1, { it is ChildNode<T> && it.parent == this }) { child ->
//                visitor(idx++, child)
//            }
            container.forEachIndexed(visitor)
        }
    }

    private class RootNode<T>(
        override val value: T
    ): Container<T>, ChildNodeProvider<T> {
        override val size: Int get() {
            return depthStore.values.sumOf { it.size }
        }

        override val depth: Int = 0
        override val isRoot: Boolean = true
        override val parent: Node<T>? = null
        override val container: MutableList<Container<T>> = mutableListOf()
        private val depthStore: HashMap<Int, MutableList<Node<T>>> = HashMap()

        init {
            depthStore.getOrPut(0) { mutableListOf() }.add(this)
        }

        override fun add(value: T): Node<T> {
            return ChildNode(value, depth + 1, this, this).also {
                added(it)
                container.add(it)
            }
        }
        override fun added(childNode: Node<T>) {
            depthStore.getOrPut(childNode.depth) { mutableListOf() }.add(childNode)
        }

        override fun children(visitor: (Int, Node<T>) -> Unit) {
            depthStore[1]?.forEachIndexed(visitor)
        }

        override fun get(index: Int, depth: Int): Node<T>? {
            return depthStore[depth]?.getOrNull(index)
        }
        override fun firstIndexOf(depth: Int, matching: (Node<T>) -> Boolean): Int {
            return depthStore[depth]?.indexOfFirst(matching) ?: -1
        }
        override fun getAll(depth: Int, matching: (Node<T>) -> Boolean, visitor: (Node<T>) -> Unit) {
            depthStore[depth]?.forEach { node ->
                if (matching(node)) visitor(node)
            }
        }

        companion object {
            @Suppress("LoopWithTooManyJumpStatements")
            fun <T> depthFirstIterator(root: RootNode<T>, maxDepth: Int, visitor: (Int, Node<T>) -> Unit) {
                var idx = 0
//                val arr = root.depthStore
//                val skip = mutableListOf<Int>()
//                val indices = mutableMapOf<Int, Int>()
//
//                while (true) {
//                    if (skip.size == arr.size) break
//                    for ((depth, store) in arr.values.withIndex()) {
//                        if (depth in skip) continue
//                        if (maxDepth <= depth) break
//                        val node = store.getOrNull(indices.getAndIncrement(depth))
//                        if (node != null) {
//                            visitor(idx++, node)
//                        } else {
//                            skip += depth
//                        }
//                    }
//                }
                fun iterate(node: Container<T>) {
                    if (maxDepth <= node.depth) return

                    visitor(idx++, node)
                    node.container.onEach { iterate(it) }
                }

                iterate(root)
            }
            fun <T> breathFirstIterator(root: RootNode<T>, maxDepth: Int, visitor: (Int, Node<T>) -> Unit) {
                var idx = 0
                visitor(idx++, root)
                val store = root.depthStore
                for (i in (1 until store.size)) {
                    if (maxDepth <= i) return
                    println("depth: $i -- nodes: ${store[i]?.size}")
                    store[i]?.forEach { visitor(idx++, it) }
                }

//                val store = ConcatenatedIterator(SingleIterator<Container<T>>(root))
//                while (store.hasNext()) {
//                    val node = store.next()
//                    if (maxDepth <= node.depth) break
//                    store + node.container
//                    visitor(idx++, node)
//                }
            }
        }
    }
}

private fun MutableMap<Int, Int>.getAndIncrement(key: Int): Int {
    return getOrDefault(key, 0).also { this[key] = it + 1 }
}
