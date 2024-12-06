
import com.github.leokash.adventofcode.utils.collections.mapInts
import com.github.leokash.adventofcode.utils.readLines
import com.github.leokash.adventofcode.utils.matrix.fold
import com.github.leokash.adventofcode.utils.matrix.Matrix
import com.github.leokash.adventofcode.utils.matrix.asMatrix

private val rgx = """\s+""".toRegex()
private data class Square(val number: Int, var selected: Boolean = false)

private typealias BingoBoard = Matrix<Square>

private class Game(input: List<String>) {
    private val numbers = mutableListOf<Int>()
    private val bingoBoards = mutableListOf<BingoBoard>()

    init {
        input[0].trim().split(',').forEach { numbers += it.toInt() }
        input.drop(1).asSequence().filter { it.isNotEmpty() }.chunked(5).forEach { values ->
            bingoBoards += values.mapInts(rgx).asMatrix { Square(it) }
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

private val BingoBoard.hasWon: Boolean get() {
    for (x in (0 until rows))
        if (row(x).count { it.selected } == rows) return true
    for (y in (0 until  columns))
        if (column(y).count { it.selected } == columns) return true
    return false
}

private fun BingoBoard.add(number: Int): Boolean {
    for ((pos, square) in this) {
        if (square.number != number)
            continue
        square.selected = true
        return row(pos.x).count { it.selected } == rows || column(pos.y).count { it.selected } == columns
    }

    return false
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
