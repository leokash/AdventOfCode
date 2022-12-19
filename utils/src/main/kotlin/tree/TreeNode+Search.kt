
@file:Suppress("Unused", "TooManyFunctions")

package tree

import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

fun <T> TreeNode<T>.all(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    selector: (TreeNode.Node<T>) -> Boolean
): Boolean {
    var result = true
    forEach(type, maxDepth) { node ->
        if (!selector(node)) { result = false; return@forEach }
    }

    return result
}

fun <T> TreeNode<T>.any(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    selector: (TreeNode.Node<T>) -> Boolean
): Boolean {
    var result = false
    forEach(type, maxDepth) { node ->
        if (selector(node)) { result = true; return@forEach }
    }

    return result
}

fun <T> TreeNode<T>.filter(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    predicate: (TreeNode.Node<T>) -> Boolean
): List<TreeNode.Node<T>> {
    return filterIndexed(maxDepth, type) { _, node -> predicate(node) }
}

fun <T> TreeNode<T>.filterIndexed(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    predicate: (Int, TreeNode.Node<T>) -> Boolean
): List<TreeNode.Node<T>> {
    return buildList {
        this@filterIndexed.forEachIndexed(type, maxDepth) { idx, node ->
            if (predicate(idx, node)) add(node)
        }
    }
}

fun <T> TreeNode<T>.sumOf(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    selector: (TreeNode.Node<T>) -> Long
): Long  {
    var count = 0L
    forEach(type, maxDepth) { count += selector(it) }

    return count
}

fun <T> TreeNode<T>.countOf(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    selector: (TreeNode.Node<T>) -> Boolean
): Int {
    var count = 0
    forEach(type, maxDepth) { if (selector(it)) count++ }

    return count
}

fun <T: Comparable<T>> TreeNode<T>.first(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    predicate: (TreeNode.Node<T>) -> Boolean
): TreeNode.Node<T>? {
    var result: TreeNode.Node<T>? = null
    forEach(type, maxDepth) { node ->
        if (predicate(node)) { result = node; return@forEach }
    }

    return result
}

fun <T, P: Comparable<P>> TreeNode<T>.minBy(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    selector: (TreeNode.Node<T>) -> P
): TreeNode.Node<T> {
    var min: TreeNode.Node<T>? = null
    forEachIndexed(type, maxDepth) { idx, node ->
        min = when (idx) {
            0 -> node
            else -> {
                val lhs = selector(min!!)
                val rhs = selector(node)
                if (lhs <= rhs) min else node
            }
        }
    }

    return min ?: error("min should be returned")
}

fun <T, P: Comparable<P>> TreeNode<T>.maxBy(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.BREATH_FIRST,
    selector: (TreeNode.Node<T>) -> P
): TreeNode.Node<T> {
    var max: TreeNode.Node<T>? = null
    forEachIndexed(type, maxDepth) { idx, node ->
        max = when (idx) {
            0 -> node
            else -> {
                val lhs = selector(max!!)
                val rhs = selector(node)
                if (lhs >= rhs) max else node
            }
        }
    }

    return max ?: error("max should be returned")
}

@Suppress("MagicNumber")
@OptIn(ExperimentalTime::class)
fun main() {
    val rand = Random(System.currentTimeMillis())
    val depth = 22
    val children = 2..5
    val selector = 2..2
    val provider = { rand.nextInt(1, 1_000_000) }
    val tree = TreeNode(provider()).also { generate(it.first(), depth, rand, children, selector, provider) }

    println(tree.size)
    println("Depth first: ")
    println(measureTimedValue { tree.countOf(type = TreeVisitorType.DEPTH_FIRST) { true } })

    println("Breath first: ")
    println(measureTimedValue { tree.countOf { true } })
}

@Suppress("LongParameterList")
private fun <T> generate(
    node: TreeNode.Node<T>,
    maxDepth: Int,
    rand: Random,
    children: IntRange,
    selector: IntRange, provider: () -> T
) {
    if (maxDepth <= node.depth) return
    repeat(rand.nextInt(children)) { node.add(provider()) }
    val indices = buildList { node.children { idx, _ -> add(idx) } }.shuffled().take(rand.nextInt(selector))
    node.children { idx, child -> if (idx in indices) generate(child, maxDepth, rand, children, selector, provider) }
}
