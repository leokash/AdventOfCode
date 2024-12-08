package com.github.leokash.adventofcode.utils.collections

import com.github.leokash.adventofcode.utils.Direction
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

fun List<CharSequence>.indicesOf(char: Char): List<Point<Int>> = indicesOf { _, c: Char -> c == char }
fun List<CharSequence>.indicesOf(predicate: (Char) -> Boolean): List<Point<Int>> = indicesOf { _, c -> predicate(c) }
fun List<CharSequence>.indicesOf(predicate: (Point<Int>, Char) -> Boolean): List<Point<Int>> = buildList {
    for (x in this@indicesOf.indices)
        for (y in this@indicesOf[x].indices)
            with(Point(x, y)) { if (predicate(this, this@indicesOf[x][y])) add(this) }
}

fun List<CharSequence>.indexOf(char: Char): Point<Int>? {
    for (x in this@indexOf.indices)
        for (y in this@indexOf[x].indices)
            if (char == this@indexOf[x][y]) return Point(x, y)

    return null
}

fun List<CharSequence>.neighbors(
    p: Point<Int>,
    allowDiagonal: Boolean = false,
    predicate: (Point<Int>, Char) -> Boolean = { _,_ -> true }
) = neighbors(p.x, p.y, allowDiagonal) { x, y, c -> predicate(Point(x, y), c) }

fun List<CharSequence>.neighbors(
x: Int,
y: Int,
allowDiagonal: Boolean = false,
predicate: (Int, Int, Char) -> Boolean = { _,_,_ -> true }
): List<Pair<Point<Int>, Char>> {
    fun attemptNeighborAddition(r: Int, c: Int, list: MutableList<Pair<Point<Int>, Char>>) {
        if (r !in indices || c !in this[0].indices) return
        get(r, c).also { value -> if (predicate(r, c, value)) list.add(Point(r, c) to value) }
    }

    return buildList {
        Direction.cardinals.map { it.point }.onEach { attemptNeighborAddition(x + it.x, y + it.y, this) }
        if (allowDiagonal) Direction.ordinals.map { it.point }.onEach { attemptNeighborAddition(x + it.x, y + it.y, this) }
    }
}
