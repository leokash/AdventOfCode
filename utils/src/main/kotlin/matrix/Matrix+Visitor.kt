package matrix

import Point

val Matrix<*>.lastRowIndex: Int get() = rows - 1
val Matrix<*>.lastColumnIndex: Int get() = columns - 1
val Matrix<*>.rowIndices: IntRange get() = 0..lastRowIndex
val Matrix<*>.columnIndices: IntRange get() = 0..lastColumnIndex

fun <T> Matrix<T>.getOrNull(point: Point): T? {
    return getOrNull(point.x, point.y)
}
fun <T> Matrix<T>.getOrNull(x: Int, y: Int): T? {
    return if (x !in rowIndices || y !in columnIndices) null else get(x, y)
}

fun Matrix<*>.contains(x: Int, y: Int): Boolean {
    return x in rowIndices && y in columnIndices
}
operator fun Matrix<*>.contains(point: Point): Boolean {
    return contains(point.x, point.y)
}

fun <T> Matrix<T>.neighbors(
    x: Int,
    y: Int,
    allowDiagonal: Boolean = false,
    predicate: (Int, Int, T) -> Boolean = { _,_,_ -> true }
): List<Pair<Point, T>> {
    fun attemptNeighborAddition(r: Int, c: Int, list: MutableList<Pair<Point, T>>) {
        getOrNull(r, c)?.also { value -> if (predicate(r, c, value)) list.add(Point(r, c) to value) }
    }

    return buildList {
        attemptNeighborAddition(x - 1, y, this)
        attemptNeighborAddition(x, y - 1, this)
        attemptNeighborAddition(x + 1, y, this)
        attemptNeighborAddition(x, y + 1, this)
        if (allowDiagonal) {
            attemptNeighborAddition(x - 1, y - 1, this)
            attemptNeighborAddition(x - 1, y + 1, this)
            attemptNeighborAddition(x + 1, y - 1, this)
            attemptNeighborAddition(x + 1, y + 1, this)
        }
    }
}
