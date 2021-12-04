
package matrix

abstract class Matrix<T> constructor(val rows: Int, val columns: Int) {
    protected abstract val store: Array<Array<T>>

    abstract fun init(f: (Int, Int) -> T)

    fun <R> fold(initial: R, f: (R, T) -> R): R {
        var acc = initial
        forEach {
            acc = f(acc, it)
        }

        return acc
    }

    inline fun forEach(f: (T) -> Unit) {
        forEachIndexed { _, _, v -> f(v) }
    }
    inline fun forEachIndexed(f: (Int, Int, T) -> Unit) {
        `access$store`.forEachIndexed { x, arr ->
            arr.forEachIndexed { y, v ->
                f(x, y, v)
            }
        }
    }

    operator fun get(x: Int, y: Int): T {
        return store[x][y]
    }
    operator fun set(x: Int, y: Int, value: T) {
        store[x][y] = value
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

    @PublishedApi
    internal val `access$store`: Array<Array<T>>
        get() = store
}
