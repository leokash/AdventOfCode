package matrix

class IntMatrix(
    rows: Int,
    columns: Int,
    init: (Int, Int) -> Int = { _, _ -> 0 }
): Matrix<Int>(rows, columns) {
    override lateinit var store: Array<Array<Int>>
    init {
        store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
    }
}

class LongMatrix(
    rows: Int,
    columns: Int,
    init: (Int, Int) -> Long = { _, _ -> 0L}
): Matrix<Long>(rows, columns) {
    override lateinit var store: Array<Array<Long>>
    init {
        store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
    }
}
