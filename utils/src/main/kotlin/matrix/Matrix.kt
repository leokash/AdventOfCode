
package matrix

import Point

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
        return store[index].slice(range ?: rowIndices)
    }
    fun column(index: Int, range: IntRange? = null): List<T> {
        if (index < 0 || index >= columns)
            throw IllegalAccessException("Out of bounds! $index not in 0..$columns")
        return store.map { it[index] }.slice(range ?: columnIndices)
    }

    operator fun get(point: Point): T {
        return get(point.x, point.y)
    }
    operator fun get(x: Int, y: Int): T {
        return store[x][y]
    }
    operator fun set(x: Int, y: Int, value: T) {
        store[x][y] = value
    }
}
