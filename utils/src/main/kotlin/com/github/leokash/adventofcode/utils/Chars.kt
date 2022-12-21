
package com.github.leokash.adventofcode.utils

const val SPRITE_ON = '\u2593'
const val SPRITE_OFF = '\u2591'

operator fun Char.plus(otherChars: List<Char>): List<Char> {
    return buildList {
        add(this@plus)
        addAll(otherChars)
    }
}
