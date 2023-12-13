
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 21L
private const val PART_TWO_EXPECTED = 525152L

fun main() {
    Logger.debug = true
    fun compute(input: List<String>): Long {
        data class State(val cursor: Int, val groupIdx: Int, val remSize: Int) {
            fun next(): State {
                return State(cursor + 1, groupIdx, -1)
            }
            fun `continue`(): State {
                return State(cursor + 1, groupIdx, remSize - 1)
            }
            fun nextGroup(rem: Int): State {
                return State(cursor + 1, groupIdx + 1, rem)
            }

            fun validate(groups: List<Int>): Boolean {
                return remSize <= 0 && groupIdx == groups.size
            }
        }

        fun String.arrangements(s: State, groups: List<Int>, cache: MutableMap<State, Long>): Long {
            return cache.getOrPut(s) {
                if (s.cursor >= length) {
                    if (s.validate(groups)) 1 else 0
                } else {
                    var sum = 0L
                    when (this[s.cursor]) {
                        '.' -> sum += if (s.remSize <= 0) arrangements(s.next(), groups, cache) else 0
                        '#' -> when { // broken
                            s.remSize > 0 -> sum += arrangements(s.`continue`(), groups, cache)
                            s.remSize < 0 -> sum += s.groupIdx.let { if (it >= groups.size) 0 else arrangements(s.nextGroup(groups[it] - 1), groups, cache) }
                        }
                        '?' -> sum += when (val rem = s.remSize) { // unknown
                            0 -> arrangements(s.next(), groups, cache)
                            else -> if (rem < 0) s.groupIdx.let { if (it >= groups.size) 0 else arrangements(s.nextGroup(groups[it] - 1), groups, cache) } + arrangements(s.next(), groups, cache) else arrangements(s.`continue`(), groups, cache)
                        }
                    }

                    sum
                }
            }
        }

        return input
            .map { s -> s.split(" ").let { it[0] to it[1].trim().split(",").map { n -> n.toInt() } } }
            .sumOf { (string, groups) -> string.arrangements(State(0, 0, -1), groups, mutableMapOf()) }
    }

    fun List<String>.unfold(): List<String> = buildList {
        for (s in this@unfold) {
            val (lhs, rhs) = s.split(" ")
            add("${List(5) { lhs }.joinToString("?")} ${List(5) { rhs }.joinToString(",") }")
        }
    }

    fun part1(input: List<String>): Long = compute(input)
    fun part2(input: List<String>): Long = compute(input.unfold())

    val input = readLines("Day12")
    val inputTest = readLines("Day12-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
