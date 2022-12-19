
package tree

fun <T, R> TreeNode<T>.map(
    type: TreeVisitorType = TreeVisitorType.DEPTH_FIRST,
    maxDepth: Int = Int.MAX_VALUE,
    selector: (TreeNode.Node<T>) -> R
): List<R> {
    return mapIndexed(type, maxDepth) { _, node -> selector(node) }
}

fun <T, R> TreeNode<T>.mapIndexed(
    type: TreeVisitorType = TreeVisitorType.DEPTH_FIRST,
    maxDepth: Int = Int.MAX_VALUE,
    selector: (Int, TreeNode.Node<T>) -> R
): List<R> {
    return buildList {
        forEachIndexed(type, maxDepth) { idx, node -> add(selector(idx, node)) }
    }
}

fun <T, R> TreeNode<T>.fold(
    initial: R,
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.DEPTH_FIRST,
    selector: (R, TreeNode.Node<T>) -> R
): R {
    return foldIndexed(initial, maxDepth, type) { _, acc, node -> selector(acc, node) }
}

fun <T, R> TreeNode<T>.foldIndexed(
    initial: R,
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.DEPTH_FIRST,
    selector: (Int, R, TreeNode.Node<T>) -> R
): R {
    var result = initial
    forEachIndexed(type, maxDepth) { idx, node ->
        result = selector(idx, result, node)
    }

    return result
}

fun <K, T> TreeNode<T>.groupBy(
    maxDepth: Int = Int.MAX_VALUE,
    type: TreeVisitorType = TreeVisitorType.DEPTH_FIRST,
    selector: (TreeNode.Node<T>) -> K
): Map<K, List<TreeNode.Node<T>>> {
    return buildMap {
        forEach(type, maxDepth) { node ->
            val key = selector(node)
            val list = getOrPut(key) { mutableListOf() }
            (list as? MutableList<TreeNode.Node<T>>)?.add(node)
        }
    }
}
