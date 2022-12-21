
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_CHUNK_SIZE = 4
private const val PART_TWO_CHUNK_SIZE = 14
private val expectedStartsForPartOne = listOf(7, 5, 6, 10, 11)
private val expectedStartsForPartTwo = listOf(19, 23, 23, 29, 26)

fun main() {
    fun part1(input: String): Int = findStart(input, PART_ONE_CHUNK_SIZE)
    fun part2(input: String): Int = findStart(input, PART_TWO_CHUNK_SIZE)

    val input = readText("Day06")
    val inputTest = readLines("Day06-Test")

    inputTest.forEachIndexed { idx, string -> check(part1(string) == expectedStartsForPartOne[idx]) }
    println(part1(input))

    inputTest.forEachIndexed { idx, string -> check(part2(string) == expectedStartsForPartTwo[idx]) }
    println(part2(input))
}

private fun findStart(input: String, chunks: Int): Int {
    for (i in (chunks..input.lastIndex))
        if (input.substring(i - chunks, i).toSet().size == chunks) return i
    return -1
}
