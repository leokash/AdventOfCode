
package com.github.leokash.adventofcode.utils

import com.github.leokash.adventofcode.utils.math.geometry.Point
import kotlin.math.abs

enum class Direction(val point: Point<Int>) {
    NORTH(Point(-1, 0)),
    NORTH_EAST(Point(-1, 1)),
    EAST(Point(0, 1)),
    SOUTH_EAST(Point(1, 1)),
    SOUTH(Point(1, 0)),
    SOUTH_WEST(Point(1, -1)),
    WEST(Point(0, -1)),
    NORTH_WEST(Point(-1, -1));

    override fun toString() = when (this) {
        EAST -> "→"
        WEST -> "←"
        NORTH -> "↑"
        SOUTH -> "↓"
        NORTH_EAST -> "↗"
        NORTH_WEST -> "↖"
        SOUTH_EAST -> "↘"
        SOUTH_WEST -> "↙"
    }

    companion object {
        val all: List<Direction> = entries
        val cardinals: List<Direction> = listOf(NORTH, EAST, SOUTH, WEST)
        val ordinals: List<Direction> = listOf(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST)
    }

    fun rotate(degrees: Int): Direction {
        if (degrees == 0 || degrees == 360)
            return this

        val turns = abs(degrees) / 45
        val count = CircularCounter(0, 7, this.ordinal)
        (if (degrees < 0) count::repeatDecrement else count::repeatIncrement)(turns)

        return entries[count.get()]
    }

    val opposite: List<Direction> get() = when(this) {
        EAST -> listOf(WEST)
        WEST -> listOf(EAST)
        NORTH -> listOf(SOUTH)
        SOUTH -> listOf(NORTH)
        NORTH_EAST -> listOf(SOUTH, WEST)
        NORTH_WEST -> listOf(SOUTH, EAST)
        SOUTH_WEST -> listOf(NORTH, EAST)
        SOUTH_EAST -> listOf(NORTH, WEST)
    }
}
