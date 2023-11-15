
package com.github.leokash.adventofcode.utils.math

infix fun Int.isEdge(range: IntRange): Boolean {
    return this == range.first || this == range.last
}

fun Int.makeEven(roundUp: Boolean = true): Int {
    return when (this % 2 == 0) {
        true -> this
        else -> if (roundUp) this + 1 else this -1
    }
}
