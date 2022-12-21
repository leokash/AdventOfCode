
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 2
private const val PART_TWO_EXPECTED = 4

private typealias IntRangePair = Pair<IntRange, IntRange>

fun main() {
    fun compute(input: List<String>, predicate: (IntRangePair) -> Boolean): Int = input
        .map { it.split(",") }
        .map { (lhs, rhs) -> lhs.toRange to rhs.toRange }
        .count(predicate)

    fun part1(input: List<String>): Int = compute(input) { (lhs, rhs) -> lhs in rhs || rhs in lhs }
    fun part2(input: List<String>): Int = compute(input) { (lhs, rhs) -> lhs intersects rhs }

    val input = readLines("Day04")
    val inputTest = readLines("Day04-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}

private val String.toRange: IntRange get() = split("-").let { (lhs, rhs) -> IntRange(lhs.toInt(), rhs.toInt()) }
