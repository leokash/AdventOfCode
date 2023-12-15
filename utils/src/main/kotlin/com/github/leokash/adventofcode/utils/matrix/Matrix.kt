
package com.github.leokash.adventofcode.utils.matrix

import com.github.leokash.adventofcode.utils.math.geometry.Point

abstract class Matrix<T> {

    abstract val rows: Int
    abstract val columns: Int
    protected abstract val store: Array<Array<T>>

    fun size(): Int = rows * columns

    fun forEach(f: (T) -> Unit) {
        forEachIndexed { _, _, v -> f(v) }
    }
    fun forEachIndexed(f: (Int, Int, T) -> Unit) {
        store.forEachIndexed { x, arr ->
            arr.forEachIndexed { y, v ->
                f(x, y, v)
            }
        }
    }

    fun row(index: Int, range: IntRange? = null): List<T> {
        if (index < 0 || index >= rows)
            throw IllegalAccessException("Out of bounds! $index not in 0..$rows")
        return store[index].slice(range ?: columnIndices)
    }
    fun column(index: Int, range: IntRange? = null): List<T> {
        if (index < 0 || index >= columns)
            throw IllegalAccessException("Out of bounds! $index not in 0..$columns")
        return store.map { it[index] }.slice(range ?: rowIndices)
    }

    operator fun get(point: Point<Int>): T {
        return get(point.x, point.y)
    }
    operator fun get(x: Int, y: Int): T {
        return store[x][y]
    }
    operator fun set(point: Point<Int>, value: T) {
        set(point.x, point.y, value)
    }
    operator fun set(x: Int, y: Int, value: T) {
        store[x][y] = value
    }

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
}
