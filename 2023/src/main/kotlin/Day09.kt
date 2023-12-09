
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.mapLongs

private const val PART_ONE_EXPECTED = 114L
private const val PART_TWO_EXPECTED = 2L

private val space = """\s+""".toRegex()

fun main() {
    Logger.debug = true
    fun compute(input: List<String>, partOne: Boolean): Long {
        val op: (Long, Long) -> Long = if (partOne) Long::plus else Long::minus
        val getter: (List<Long>) -> Long = if (partOne) ({ it[it.size - 1] }) else ({ it[0] })

        fun findNextValue(list: List<Long>): Long {
            val intervals = list.windowed(2).map { it[1] - it[0] }
            return op(getter(list), if (intervals.distinctBy { it }.size == 1) intervals[0] else findNextValue(intervals))
        }

        return input.sumOf { findNextValue(it.mapLongs(space)) }
    }

    fun part1(input: List<String>): Long = compute(input, true)
    fun part2(input: List<String>): Long = compute(input, false)

    val input = readLines("Day09")
    val inputTest = readLines("Day09-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
