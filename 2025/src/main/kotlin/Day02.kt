
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 1227775554L
private const val PART_TWO_EXPECTED = 4174379265L

private fun Long.isInvalidId(partOne: Boolean): Boolean {
    fun validate(id: String, size: Int): Boolean {
        val part = id.take(size)
        return id.chunked(size).all { it == part }
    }

    val id = this.toString()
    if (partOne) {
        return when (id.length % 2) {
            0 -> (id.length / 2).let { half -> id.take(half) == id.substring(half) }
            else -> false
        }
    }

    for (i in 1..(id.length / 2)) {
        if (validate(id, i))
            return true
    }

    return false
}

fun main() {
    fun compute(input: List<String>, partOne: Boolean = true): Long {
        return input
            .flatMap { it.split("-").let { parts -> parts.first().toLong() .. parts.last().toLong() } }
            .filter { it.isInvalidId(partOne) }
            .sum()
    }

    fun part1(input: List<String>): Long = compute(input)
    fun part2(input: List<String>): Long = compute(input, false)

    val input = readText("Day02").split(",").map { it.trim() }
    val inputTest = readText("Day02-Test").split(",").map { it.trim() }

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
