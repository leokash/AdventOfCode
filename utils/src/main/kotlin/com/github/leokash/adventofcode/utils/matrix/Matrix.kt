
package com.github.leokash.adventofcode.utils.matrix

import com.github.leokash.adventofcode.utils.Stopper
import com.github.leokash.adventofcode.utils.math.geometry.Point

class Matrix<T>(val rows: Int, val columns: Int, init: (Int, Int) -> T): Iterable<Pair<Point<Int>, T>> {
    private val store = mutableMapOf<Point<Int>, T>()

    init {
        for (x in rowIndices)
            for (y in columnIndices)
                store[Point(x, y)] = init(x, y)
    }

    fun size(): Int = rows * columns
    fun forEach(f: (T) -> Unit) {
        for ((_, value) in this)
            f(value)
    }
    fun forEachIndexed(f: (Int, Int, T) -> Unit) {
        for ((pos, value) in this)
            f(pos.x, pos.y, value)
    }
    fun forEachIndexed(f: (Point<Int>, T) -> Unit) {
        for ((pos, value ) in this)
            f(pos, value)
    }

    fun row(index: Int, range: IntRange? = null): List<T> {
        if (index < 0 || index >= rows)
            throw IllegalAccessException("Out of bounds! $index not in 0..$rows")
        return (range ?: columnIndices).let { (it.first..it.last).map { y -> get(index, y) } }
    }
    fun column(index: Int, range: IntRange? = null): List<T> {
        if (index < 0 || index >= columns)
            throw IllegalAccessException("Out of bounds! $index not in 0..$columns")
        return (range ?: rowIndices).let { (it.first..it.last).map { x -> get(x, index) } }
    }

    fun scan(rows: Int, columns: Int, window: (Point<Int>, Stopper, List<List<T>>) -> Unit) {
        val s = Stopper()
        for ((p, _) in this) {
            if (s.flag) break
            if (p.x + rows >= this.rows) continue
            if (p.y + columns >= this.columns) continue
            window(p, s, buildList {
                for (x in p.x..<(p.x + rows))
                    add(row(x, p.y..<(p.y + columns)))
            })
        }
    }

    fun slice(p: Point<Int>, rows: Int, columns: Int): Matrix<T>? {
        if (p.x !in rowIndices || p.x + (rows - 1) >= this.rows) return null
        if (p.y !in columnIndices || p.y + (columns - 1) >= this.columns) return null
        return Matrix(rows, columns) { i, j -> get(p.x + i, p.y + j) }
    }

    @Suppress("unused")
    fun slice(x: Int, y: Int, rows: Int, columns: Int): Matrix<T>? {
        return slice(Point(x, y), rows, columns)
    }

    operator fun get(x: Int, y: Int): T = get(Point(x, y))
    operator fun get(point: Point<Int>): T = store.getValue(point)

    operator fun set(x: Int, y: Int, value: T) = set(Point(x, y), value)
    operator fun set(point: Point<Int>, value: T) = store.also { it[point] = value }

    override fun hashCode(): Int {
        var result = 0
        for (x in rowIndices)
            for (y in columnIndices)
                result = 31 * result + this[x, y].hashCode()
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix<*>) return false

        if (rows != other.rows) return false
        if (columns != other.columns) return false

        for (x in rowIndices)
            for (y in columnIndices)
                if (this[x, y] != other[x, y]) return false

        return true
    }
    override fun iterator(): Iterator<Pair<Point<Int>, T>> {
        return object: Iterator<Pair<Point<Int>, T>> {
            var curX: Int = 0
            var curY: Int = 0
            override fun hasNext(): Boolean {
                return curX in rowIndices
            }

            override fun next(): Pair<Point<Int>, T> {
                return Point(curX, curY) to get(curX, curY).also {
                    val tmp = curY + 1
                    curY = if (tmp in columnIndices) tmp else 0.also { curX++ }
                }
            }
        }
    }

    companion object Factory
}
