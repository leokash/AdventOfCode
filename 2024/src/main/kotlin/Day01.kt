
import com.github.leokash.adventofcode.utils.*
import kotlin.math.abs

private const val PART_ONE_EXPECTED = 11L
private const val PART_TWO_EXPECTED = 31L

private fun parse(input: List<String>): Pair<List<Long>, List<Long>> = input
    .map { it.split("\\s+".toRegex()) }
    .fold(mutableListOf<Long>() to mutableListOf<Long>()) { acc, list -> acc.apply {  this.first.add(list[0].toLong()); this.second.add(list[1].toLong()) } }

fun main() {
    fun part1(input: List<String>): Long = parse(input)
        .let { it.first.sorted() to it.second.sorted() }
        .let { (lhs, rhs) -> lhs.foldIndexed(0L) {idx, acc, num -> acc + abs(num - rhs[idx]) } }

    fun part2(input: List<String>): Long = parse(input)
        .let { (lhs, rhs) -> lhs.fold(0L) { acc, num -> acc + (num * rhs.count { it == num }) } }

    val input = readLines("Day01")
    val inputTest = readLines("Day01-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
