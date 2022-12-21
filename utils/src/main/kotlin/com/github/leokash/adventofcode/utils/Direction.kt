
package com.github.leokash.adventofcode.utils

import com.github.leokash.adventofcode.utils.geometry.points.ints.Point

enum class Direction(val point: Point) {
    NORTH(Point(-1, 0)),
    NORTH_EAST(Point(-1, 1)),
    EAST(Point(0, 1)),
    SOUTH_EAST(Point(1, 1)),
    SOUTH(Point(1, 0)),
    SOUTH_WEST(Point(1, -1)),
    WEST(Point(0, -1)),
    NORTH_WEST(Point(-1, -1));

    companion object {
        val all: List<Direction> = Direction.values().toList()
        val cardinals: List<Direction> = listOf(NORTH, EAST, SOUTH, WEST)
        val ordinals: List<Direction> = listOf(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST)
    }
}
