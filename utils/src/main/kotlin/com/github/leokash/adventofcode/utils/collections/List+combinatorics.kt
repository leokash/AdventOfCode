
package com.github.leokash.adventofcode.utils.collections

fun <T> List<T>.permutations(): List<T> {
    return when (size) {
        0, 1 -> this
        else -> mapIndexed { i, lhs ->  drop(i + 1).map { rhs -> listOf(lhs, rhs, rhs, lhs) }.flatten() }.flatten()
    }
}
