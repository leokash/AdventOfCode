
import com.github.leokash.adventofcode.utils.Logger
import com.github.leokash.adventofcode.utils.collections.chunkedBy
import com.github.leokash.adventofcode.utils.collections.mapLongs
import com.github.leokash.adventofcode.utils.readLines

private const val PART_ONE_EXPECTED = 3L
private const val PART_TWO_EXPECTED = 14L

private fun List<LongRange>.consolidate(): List<LongRange> {
    return buildList {
        for (range in this@consolidate.sortedBy { it.first }) {
            val last = lastOrNull()
            if (last != null && range.first <= last.last) {
                this[lastIndex] = last.first..maxOf(last.last, range.last)
            } else {
                add(range)
            }
        }
    }
}

fun main() {
    Logger.enabled = true
    fun compute(input: List<String>, partOne: Boolean = true): Long {
        val arr = input.chunkedBy { it.isEmpty() }
        val ranges = arr[0]
            .map { s -> s.split("-").let { LongRange(it[0].toLong(), it[1].toLong()) } }
            .consolidate()

        if (partOne)
            return arr[1].mapLongs().flatMap { it }.count { id -> ranges.any { id in it } }.toLong()
        return ranges.sumOf { it.last - it.first + 1 }
    }

    fun part1(input: List<String>): Long = compute(input)
    fun part2(input: List<String>): Long = compute(input, false)

    val input = readLines("Day05")
    val inputTest = readLines("Day05-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
