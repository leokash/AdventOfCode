
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 6L
private const val PART_TWO_EXPECTED = 16L

fun main() {
    Logger.enabled = true
    fun compute(input: List<String>, findArrangements: Boolean = false): Long {
        fun count(towel: String, patterns: List<String>, cache: MutableMap<String, Long>): Long {
            return cache.getOrPut(towel) {
                if (towel.isEmpty()) 1 else patterns.sumOf { if (towel.startsWith(it)) count(towel.removePrefix(it), patterns, cache) else 0 }
            }
        }

        val patterns = input[0].split(commaRegex).map(String::trim)
        if (!findArrangements)
            return input.drop(2).count { count(it, patterns, mutableMapOf()) > 0 }.toLong()

        return input.drop(2).sumOf { count(it, patterns, mutableMapOf()) }
    }
    fun part1(input: List<String>): Long = compute(input)
    fun part2(input: List<String>): Long = compute(input, true)

    val input = readLines("Day19")
    val inputTest = readLines("Day19-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
