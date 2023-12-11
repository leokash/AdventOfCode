
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.mapLongs

private const val PART_ONE_EXPECTED = 114L
private const val PART_TWO_EXPECTED = 2L

private val space = """\s+""".toRegex()

fun main() {
    fun compute(input: List<String>, partOne: Boolean): Long {
        fun findNextValue(list: List<Long>) = generateSequence(if (partOne) list else list.reversed()) { arr -> arr.windowed(2) { (a, b) -> b - a } }
                .takeWhile { arr -> !arr.all { it == 0L } }
                .sumOf { it.last() }

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
