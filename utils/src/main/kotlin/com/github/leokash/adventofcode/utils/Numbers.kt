
@file:Suppress("unused", "UNCHECKED_CAST")

package com.github.leokash.adventofcode.utils

import com.github.leokash.adventofcode.utils.geometry.points.ints.Point as IPoint
import com.github.leokash.adventofcode.utils.geometry.points.longs.Point as LPoint
import kotlin.math.abs

val Int.abs: Int get() = abs(this)
val Long.abs: Long get() = abs(this)
val Double.abs: Double get() = abs(this)

infix fun Int.isEdge(range: IntRange): Boolean {
    return this == range.first || this == range.last
}

fun Int.makeEven(roundUp: Boolean = true): Int {
    return when (this % 2 == 0) {
        true -> this
        else -> if (roundUp) this + 1 else this -1
    }
}
fun Int.toPoint(rows: Int, columns: Int): IPoint {
    return IPoint(this / rows, this % columns)
}
fun Long.toPoint(rows: Int, columns: Int): LPoint {
    return LPoint(this / rows, this % columns)
}
