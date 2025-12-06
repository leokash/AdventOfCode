
import com.github.leokash.adventofcode.utils.Logger
import com.github.leokash.adventofcode.utils.collections.toCharMatrix
import com.github.leokash.adventofcode.utils.math.mapLongs
import com.github.leokash.adventofcode.utils.readLines
import com.github.leokash.adventofcode.utils.spaceRegex

private const val PART_ONE_EXPECTED = 4277556L
private const val PART_TWO_EXPECTED = 3263827L

private sealed interface Op {
    fun eval(lhs: Long, rhs: Long): Long

    data object Add: Op {
        override fun eval(lhs: Long, rhs: Long): Long = lhs + rhs
    }

    data object Mul: Op {
        override fun eval(lhs: Long, rhs: Long): Long = (if(lhs == 0L) 1 else lhs) * rhs
    }

    companion object {
        operator fun invoke(char: Char): Op {
            return if(char == '+') Add else Mul
        }
    }
}

private data class State(val idx: Int = 0, val sum: Long = 0, val problem: Long = 0)

fun main() {
    Logger.enabled = false
    fun part1(input: List<String>): Long {
        val ops = input.last().split(spaceRegex).map { Op(it.first()) }
        return input
            .dropLast(1)
            .map { it.mapLongs(spaceRegex) }
            .reduce { lhs, rhs -> lhs.mapIndexed { i, num -> ops[i].eval(num, rhs[i]) } }
            .sum()
    }
    fun part2(input: List<String>): Long {
        val max = input.maxOf { it.length }
        val ops = input.last().split(spaceRegex).map { Op(it[0]) }.reversed()
        val mat = input.dropLast(1).map { it.padEnd(max).reversed() }.toCharMatrix()
        return (0 .. max).fold(State()) { state, i ->
            val vector = if (i < mat.columns) mat.column(i).joinToString("").trim() else ""
            when (vector.length) {
                0 -> State(state.idx + 1, state.sum + state.problem)
                else -> State(state.idx, state.sum, ops[state.idx].eval(state.problem, vector.toLong()))
            }
        }.sum
    }

    val input = readLines("Day06")
    val inputTest = readLines("Day06-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
