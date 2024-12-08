
package com.github.leokash.adventofcode.utils.math.geometry

import kotlin.math.abs
import kotlin.math.pow

fun <T> Point<T>.chebyshevDistance(rhs: Point<T>): T where T: Number, T: Comparable<T> = with(context) {
    return max(abs(sub(x, rhs.x)), abs(sub(y, rhs.y)))
}

@Suppress("unused")
fun <T> Point<T>.euclideanDistance(rhs: Point<T>): T where T: Number, T: Comparable<T> = with(context) {
    return map(kotlin.math.sqrt(abs((x.toDouble() - rhs.x.toDouble()).pow(2)) + abs((y.toDouble() - rhs.y.toDouble()).pow(2))))
}

fun <T> Point<T>.manhattanDistance(rhs: Point<T>): T where T: Number, T: Comparable<T> = with(context) {
    return add(abs(sub(x, rhs.x)), abs(sub(y, rhs.y)))
}
