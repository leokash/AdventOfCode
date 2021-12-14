
package matrix

abstract class Matrix<T> constructor(val rows: Int, val columns: Int) {

    val size: Int = rows * columns
    protected abstract val store: Array<Array<T>>

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

    fun row(index: Int): List<T> {
        if (index < 0 || index >= rows)
            throw IllegalAccessException("Out of bounds! $index not in 0..$rows")
        return store[index].toList()
    }
    fun column(index: Int): List<T> {
        if (index < 0 || index >= columns)
            throw IllegalAccessException("Out of bounds! $index not in 0..$columns")
        return store.map { it[index] }.toList()
    }

    operator fun get(x: Int, y: Int): T {
        return store[x][y]
    }
    operator fun set(x: Int, y: Int, value: T) {
        store[x][y] = value
    }
}
