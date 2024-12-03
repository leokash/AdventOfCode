
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 161L
private const val PART_TWO_EXPECTED = 48L

private val partOneRegex = """mul\(\d+,\d+\)""".toRegex()
private val partTwoRegex = """mul\(\d+,\d+\)|(do(n't)?)\(\)""".toRegex()

fun main() {
    fun compute(input: String, regex: Regex): Long {
        var skip = false
        return input.findAll(regex).fold(0L) { acc, match ->
            acc + when(match) {
                "do()" -> 0L.also { skip = false }
                "don't()" -> 0L.also { skip = true }
                else -> if (skip) 0L else match.removeAll("mul(", ")").split(",").let { it[0].toLong() * it[1].toLong() }
            }
        }
    }

    fun part1(input: String): Long = compute(input, partOneRegex)
    fun part2(input: String): Long = compute(input, partTwoRegex)

    val input = readText("Day03")
    val inputTest = readText("Day03-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
