
package com.github.leokash.adventofcode.utils.math.geometry

import com.github.leokash.adventofcode.utils.Direction

fun Int.toPoint(rows: Int, columns: Int): Point<Int> {
    return Point(this / rows, this % columns)
}

fun Long.toPoint(rows: Int, columns: Int): Point<Long> {
    return Point(this / rows, this % columns)
}

fun <T> Point<T>.lineTo(other: Point<T>): List<Point<T>> where T: Number, T: Comparable<T> = with(context) {
    val xd = sign(sub(other.x, x))
    val yd = sign(sub(other.y, y))
    val steps = chebyshevDistance(other)

    return (1..steps.toInt()).scan(this@lineTo) { p, _ -> Point(add(p.x, xd), add(p.y, yd), this) }
}

fun <T> Point<T>.directionTo(other: Point<T>): Direction? where T: Number, T: Comparable<T> {
    return if (x == other.x && y == other.y) null else when {
        x == other.x -> if (y < other.y) Direction.EAST else Direction.WEST
        y == other.y -> if (x < other.x) Direction.SOUTH else Direction.NORTH
        else -> {
            if (x < other.x)
                if (y < other.y) Direction.SOUTH_EAST else Direction.SOUTH_WEST
            else
                if (y < other.y) Direction.NORTH_EAST else Direction.NORTH_WEST
        }
    }
}

fun <T> Point<T>.move(steps: Int = 1, direction: Direction, bounds: Rect<T>? = null): Point<T> where T: Number, T: Comparable<T> {
    var initial = this
    repeat(steps) {
        initial = initial.next(direction, bounds) ?: return initial
    }

    return initial
}

fun <T> Point<T>.next(direction: Direction, bounds: Rect<T>? = null): Point<T>? where T: Number, T: Comparable<T> = with(context) {
    val dp = direction.point
    val tmp = Point(add(x, map(dp.x)), add(y, map(dp.y)), this)
    return if (bounds == null || tmp in bounds) tmp else null
}

fun <T> Point<T>.neighbors(acceptOrdinals: Boolean = false, bounds: Rect<T>? = null): List<Pair<Direction, Point<T>>> where T: Number, T: Comparable<T> {
    return buildList {
        addAll(Direction.cardinals.mapNotNull { d -> this@neighbors.next(d, bounds)?.let { d to it } })
        if (acceptOrdinals) addAll(Direction.ordinals.mapNotNull { d -> this@neighbors.next(d, bounds)?.let { d to it } })
    }
}
