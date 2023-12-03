package com.github.leokash.adventofcode.utils.collections

import com.github.leokash.adventofcode.utils.math.geometry.Point

operator fun <E> List<E>.component6(): E = this[5]
operator fun <E> List<E>.component7(): E = this[6]
operator fun <E> List<E>.component8(): E = this[7]
operator fun <E> List<E>.component9(): E = this[8]
operator fun <E> List<E>.component10(): E = this[9]

operator fun List<CharSequence>.get(p: Point<Int>): Char = get(p.x, p.y)
operator fun List<CharSequence>.get(x: Int, y: Int): Char = this[x][y]
operator fun List<CharSequence>.contains(p: Point<Int>): Boolean = if (p.x in indices) p.y in this[p.x].indices else false

fun List<CharSequence>.getOrNull(p: Point<Int>): Char? = getOrNull(p.x, p.y)
fun List<CharSequence>.getOrNull(x: Int, y: Int): Char? = if (x in indices) this[x].let { if (y in it.indices) it[y] else null } else null

fun List<CharSequence>.indicesOf(char: Char): List<Point<Int>> = buildList {
    for (x in this@indicesOf.indices)
        for (y in this@indicesOf[x].indices)
            if (char == this@indicesOf[x][y]) add(Point(x, y))
}
