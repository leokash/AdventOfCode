
import collections.splitBy

private const val PART_ONE_EXPECTED = 24000
private const val PART_TWO_EXPECTED = 45000
fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.toIntOrNull() }
            .splitBy { it == null }
            .maxOfOrNull { it.sumOf { num -> num ?: 0 } } ?: 0
    }
    fun part2(input: List<String>): Int {
        return input
            .map { it.toIntOrNull() }
            .splitBy { it == null }
            .map { it.sumOf { num -> num ?: 0 } }
            .sortedDescending()
            .take(3)
            .sum()
    }

    val input = readLines("Day01")
    val inputTest = readLines("Day01-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
