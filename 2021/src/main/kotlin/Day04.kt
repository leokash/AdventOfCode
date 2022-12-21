
import com.github.leokash.adventofcode.utils.readLines
import com.github.leokash.adventofcode.utils.matrix.fold
import com.github.leokash.adventofcode.utils.matrix.Matrix

private val rgx = """\s+""".toRegex()
private data class Square(val number: Int, var selected: Boolean = false)

private class Game(input: List<String>) {
    private val numbers = mutableListOf<Int>()
    private val bingoBoards = mutableListOf<BingoBoard>()

    init {
        input[0].trim().split(',').forEach { numbers += it.toInt() }
        input.drop(1).asSequence().filter { it.isNotEmpty() }.chunked(5).forEach {
            val values = it.map { s -> s.trim().split(rgx) }
            bingoBoards += BingoBoard { x, y -> Square(values[x][y].trim().toInt()) }
        }
    }

    private data class Winner(val number: Int, val index: Int) {
        override fun hashCode(): Int {
            return index
        }
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Winner) return false
            return index == other.index
        }
    }

    fun run1(): Int {
        for (num in numbers) {
            bingoBoards.forEach { board ->
                if (board.add(num)) {
                    return board.fold(0) { acc, (num, b) -> acc + if (b) 0 else num } * num
                }
            }
        }

        return 0
    }
    fun run2(): Int {
        val winningBoards = mutableSetOf<Winner>()
        for (num in numbers) {
            bingoBoards.forEachIndexed { i, board ->
                if (!board.hasWon && board.add(num)) {
                    winningBoards += Winner(num, i)
                }
            }
        }

        winningBoards.lastOrNull()?.let { (num, idx) ->
            val board = bingoBoards[idx]
            return board.fold(0) { acc, (num, b) -> acc + if (b) 0 else num } * num
        }
        return 0
    }
}
private class BingoBoard(init: (Int, Int) -> Square): Matrix<Square>() {

    override val rows: Int get() = 5
    override val columns: Int get() = 5
    override lateinit var store: Array<Array<Square>>

    val hasWon: Boolean get() {
        for (x in (0 until rows))
            if (row(x).count { it.selected } == rows) return true
        for (y in (0 until  columns))
            if (column(y).count { it.selected } == columns) return true
        return false
    }

    init {
        store = Array(rows) { x -> Array(columns) { y -> init(x, y) } }
    }

    fun add(number: Int): Boolean {
        for (x in (0 until rows)) {
            for (y in (0 until columns)) {
                if (this[x, y].number != number)
                    continue
                this[x, y].selected = true
                return row(x).count { it.selected } == rows ||
                        column(y).count { it.selected } == columns
            }
        }

        return false
    }
}

fun main() {
    fun part1(input: List<String>): Int = Game(input).run1()
    fun part2(input: List<String>): Int = Game(input).run2()

    val input = readLines("Day04")
    val inputTest = readLines("Day04-Test")

    check(part1(inputTest) == 4512)
    check(part2(inputTest) == 1924)

    println(part1(input))
    println(part2(input))
}
