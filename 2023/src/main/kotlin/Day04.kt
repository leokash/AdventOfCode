
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 13
private const val PART_TWO_EXPECTED = 30

private val spaceRgx = """\s+""".toRegex()

fun main() {
    fun Set<String>.score(set: Set<String>): Int = fold(0) { acc, num ->
        when (num in set) {
            true -> if (acc == 0) 1 else acc * 2
            else -> acc
        }
    }

    fun compute(input: List<String>, partOne: Boolean): Int {
        val pairs = input.map { card ->
            card.substringAfter(": ")
            .split(" | ")
            .map { s -> s.trim().split(spaceRgx).toSet() }
        }

        if (partOne) {
            return pairs.sumOf { (winning, numbers) -> numbers.score(winning) }
        }

        val arr = IntArray(input.size) { 1 }
        pairs.forEachIndexed { idx, pair ->
            repeat(pair[0].intersect(pair[1]).size) {
                arr[idx + 1 + it] += arr[idx]
            }
        }

        return arr.sum()
    }

    fun part1(input: List<String>): Int = compute(input, true)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day04")
    val inputTest = readLines("Day04-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
