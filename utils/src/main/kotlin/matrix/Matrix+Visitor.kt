package matrix

import Point

fun <T> Matrix<T>.neighbors(
    x: Int,
    y: Int,
    allowDiagonal: Boolean = false,
    predicate: (Int, Int, T) -> Boolean = {_,_,_ -> true}
): List<Pair<Point, T>> {
    val rowRange = 0 until rows
    val columnRange = 0 until columns
    fun attemptNeighborAddition(r: Int, c: Int, list: MutableList<Pair<Point, T>>) {
        if (r in rowRange && c in columnRange && predicate(r, c, this[r, c]))
            list += Pair(Point(r, c), this[r, c])
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
