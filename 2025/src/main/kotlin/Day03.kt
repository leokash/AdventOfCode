
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 357L
private const val PART_TWO_EXPECTED = 3121910778619L

private fun String.findBattery(size: Int): String = buildString {
    var idx = 0
    for (i in 0..< size) {
        val remaining = size - i - 1
        val searchEnd = this@findBattery.length - remaining

        var maxIdx = idx
        var maxChar = this@findBattery[idx]
        for (j in idx ..< searchEnd) {
            if (this@findBattery[j] > maxChar) {
                maxChar = this@findBattery[j]
                maxIdx = j
            }
        }

        idx = maxIdx + 1
        append(maxChar)
    }
}

fun main() {
    Logger.enabled = true
    fun compute(input: List<String>, partOne: Boolean = true): Long {
        return input.sumOf { it.findBattery(if (partOne) 2 else 12).toLong() }
    }

    fun part1(input: List<String>): Long = compute(input)
    fun part2(input: List<String>): Long = compute(input, false)

    val input = readLines("Day03")
    val inputTest = readLines("Day03-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
