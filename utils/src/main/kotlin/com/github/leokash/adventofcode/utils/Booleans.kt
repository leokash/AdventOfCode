
package com.github.leokash.adventofcode.utils

val Boolean.int: Int get() = if (this) 1 else 0
operator fun Boolean.plus(otherBooleans: List<Boolean>): List<Boolean> {
    return buildList {
        add(this@plus)
        addAll(otherBooleans)
    }
}