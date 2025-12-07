
package com.github.leokash.adventofcode.utils.matrix

import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.isEdge

val Matrix<*>.lastRowIndex: Int get() = rows - 1
val Matrix<*>.lastColumnIndex: Int get() = columns - 1

val Matrix<*>.rowIndices: IntRange get() = 0..lastRowIndex
val Matrix<*>.columnIndices: IntRange get() = 0..lastColumnIndex

fun <T> Matrix<T>.getOrNull(x: Int, y: Int): T? {
    return if (x !in rowIndices || y !in columnIndices) null else get(x, y)
}
fun <T> Matrix<T>.getOrNull(point: Point<Int>): T? {
    return getOrNull(point.x, point.y)
}

fun Matrix<*>.indexOf(p: Point<Int>): Int = indexOf(p.x, p.y)
fun Matrix<*>.indexOf(x: Int, y: Int): Int = columns * x + y

fun <T> Matrix<T>.indexOfFirst(predicate: (T) -> Boolean): Point<Int> {
    for ((idx, obj) in this)
        if (predicate(obj))
            return idx
    error("Unable to find first index of an item in ${this::class.simpleName}")
}

@Suppress("unused")
fun Matrix<*>.isEdge(p: Point<Int>): Boolean {
    return isEdge(p.x, p.y)
}
fun Matrix<*>.isEdge(x: Int, y: Int): Boolean {
    return x isEdge rowIndices || y isEdge columnIndices
}

fun Matrix<*>.contains(x: Int, y: Int): Boolean {
    return x in rowIndices && y in columnIndices
}
operator fun Matrix<*>.contains(point: Point<Int>): Boolean {
    return contains(point.x, point.y)
}

fun <T> Matrix<T>.neighbors(
    point: Point<Int>,
    allowDiagonal: Boolean = false,
    predicate: (Int, Int, T) -> Boolean = { _,_,_ -> true }
): List<Pair<Point<Int>, T>> {
    return neighbors(point.x, point.y, allowDiagonal, predicate)
}

fun <T> Matrix<T>.neighbors(
    x: Int,
    y: Int,
    allowDiagonal: Boolean = false,
    predicate: (Int, Int, T) -> Boolean = { _,_,_ -> true }
): List<Pair<Point<Int>, T>> {
    fun attemptNeighborAddition(r: Int, c: Int, list: MutableList<Pair<Point<Int>, T>>) {
        if (r !in rowIndices || c !in columnIndices) return
        get(r, c).also { value -> if (predicate(r, c, value)) list.add(Point(r, c) to value) }
    }

    return buildList {
        Direction.cardinals.map { it.point }.onEach { attemptNeighborAddition(x + it.x, y + it.y, this) }
        if (allowDiagonal) Direction.ordinals.map { it.point }.onEach { attemptNeighborAddition(x + it.x, y + it.y, this) }
    }
}
