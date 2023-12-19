
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 0
private const val PART_TWO_EXPECTED = 0

fun main() {
    fun compute(input: List<String>, partOne: Boolean): Int = TODO()

    fun part1(input: List<String>): Int = compute(input, true)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day19")
    val inputTest = readLines("Day19-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
