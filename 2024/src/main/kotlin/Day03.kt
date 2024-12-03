
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 161L
private const val PART_TWO_EXPECTED = 48L

private val flagRegex = """do(n't)?\(\)""".toRegex()
private val multiRegex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()

private typealias Match = Pair<IntRange, String>

private val Match.range: IntRange get() = first

private fun String.accept(current: Int, previous: Int?, matches: List<Match>): Boolean {
    if (previous != null) {
        return substring(matches[previous].range.last, matches[current].range.first)
            .findAll(flagRegex)
            .lastOrNull()
            ?.let { it == "do()" }
            ?: true
    }

    return true
}

fun main() {
    fun compute(input: String, filter: Boolean): Long {
        fun String.multiply(): Long = replace(mapOf("mul(" to "", ")" to ""))
            .split(",")
            .let { it[0].toLong() * it[1].toLong() }

        var previousIdx: Int? = null
        return with(input.findMatches(multiRegex)) {
            foldIndexed(0L) { idx, acc, (_, match) ->
                acc + when (filter) {
                    true -> if (input.accept(idx, previousIdx, this)) match.multiply().also { previousIdx = idx } else 0
                    false -> match.multiply()
                }
            }
        }
    }

    fun part1(input: String): Long = compute(input, false)
    fun part2(input: String): Long = compute(input, true)

    val input = readText("Day03")
    val inputTest = readText("Day03-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
