package matrix

class IntMatrix(
    override val rows: Int,
    override val columns: Int,
    init: (Int, Int) -> Int = { _, _ -> 0 }
): Matrix<Int>() {
    override lateinit var store: Array<Array<Int>>
    init {
        store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
    }
}

class LongMatrix(
    override val rows: Int,
    override val columns: Int,
    init: (Int, Int) -> Long = { _, _ -> 0L}
): Matrix<Long>() {
    override lateinit var store: Array<Array<Long>>
    init {
        store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
    }
}
