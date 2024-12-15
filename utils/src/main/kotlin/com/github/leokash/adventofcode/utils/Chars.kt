
package com.github.leokash.adventofcode.utils

const val SPRITE_ON = '\u2593'
const val SPRITE_OFF = '\u2591'

val Char.asCardinalDirection: Direction get() = when(this) {
    '^' -> Direction.NORTH
    '>' -> Direction.EAST
    'v' -> Direction.SOUTH
    '<' -> Direction.WEST
    else -> error("Invalid direction from char: $this. Only supporting cardinal directions")
}

operator fun Char.plus(otherChars: List<Char>): List<Char> {
    return buildList {
        add(this@plus)
        addAll(otherChars)
    }
}
