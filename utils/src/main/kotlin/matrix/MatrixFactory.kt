
package matrix

class IntMatrix(
    override val rows: Int,
    override val columns: Int,
    init: (Int, Int) -> Int = { _, _ -> 0 }
): Matrix<Int>() {
    override var store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
}

class CharMatrix(
    override val rows: Int,
    override val columns: Int,
    private val defaultChar: Char = ' ',
    init: (Int, Int) -> Char = { _, _ ->  defaultChar }
): Matrix<Char>() {
    override var store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
}

class LongMatrix(
    override val rows: Int,
    override val columns: Int,
    init: (Int, Int) -> Long = { _, _ -> 0L }
): Matrix<Long>() {
    override var store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
}
