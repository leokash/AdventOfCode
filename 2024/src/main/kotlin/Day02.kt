
import com.github.leokash.adventofcode.utils.*
import kotlin.math.abs
import kotlin.math.sign

private const val PART_ONE_EXPECTED = 2
private const val PART_TWO_EXPECTED = 4

private fun List<Int>.validateReport(dampen: Boolean = false): Boolean {
    val sign = (this[0] - this[1]).sign

    for (i in 1..<size) {
        val diff = this[i - 1] - this[i]
        if (abs(diff) !in 1..3 || sign != diff.sign) {
            return if (dampen) indices.any { filterIndexed { j, _ ->  j != it }.validateReport() } else false
        }
    }

    return true
}

fun main() {
    fun compute(input: List<String>, dampen: Boolean = false): Int = input
        .map { it.split("\\s+".toRegex()) }
        .count { it.map { string -> string.toInt() }.validateReport(dampen) }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    val input = readLines("Day02")
    val inputTest = readLines("Day02-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
