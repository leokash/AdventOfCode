
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 142
private const val PART_TWO_EXPECTED = 281

private val map: Map<String, Int> = mapOf(
    "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
)

private val regex = """(\d|one|t(wo|hree)|f(our|ive)|s(ix|even)|eight|nine)""".toRegex()

private fun String.toDigit(allowStringDigit: Boolean): Int? = when(this.length) {
    1 -> first().digitToInt()
    else -> if (allowStringDigit) map[this] else null
}

fun main() {
    fun compute(input: List<String>, allowStringDigit: Boolean = false): Int = input.sumOf { string ->
        string
            .findAll(regex)
            .mapNotNull { it.toDigit(allowStringDigit) }
            .let { it.first() * 10 + it.last() }
    }

    fun part1(input: List<String>) = compute(input)
    fun part2(input: List<String>) = compute(input, true)

    val input = readLines("Day01")
    check(part1(readLines("Day01-Test")) == PART_ONE_EXPECTED)
    check(part2(readLines("Day01-Test-2")) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
