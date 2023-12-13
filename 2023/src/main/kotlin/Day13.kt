
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.chunkedBy
import kotlin.math.max
import kotlin.math.min
import kotlin.text.StringBuilder

private const val PART_ONE_EXPECTED = 405L
private const val PART_TWO_EXPECTED = 400L


fun main() {
    Logger.debug = true
    fun compute(input: List<String>, partOne: Boolean): Long {
        fun List<String>.transposed(): List<String> = this[0].indices.map {
            fold(StringBuilder()) { acc, s -> acc.append(s[it]) }.toString()
        }

        fun List<String>.reflectionIndex(): Int {
            fun matches(lhs: List<String>, rhs: List<String>): Boolean {
                if (partOne) {
                    val size = min(lhs.size, rhs.size)
                    return lhs.subList(0, size) == rhs.subList(0, size)
                }

                var found = false
                for (i in (0..min(lhs.lastIndex, rhs.lastIndex))) {
                    val s1 = lhs[i]
                    val s2 = rhs[i]
                    when ((0..max(s1.lastIndex, s2.lastIndex)).count { s1.getOrNull(it) != s2.getOrNull(it) }) {
                        0 -> { }
                        1 -> if (found) return false else found = true
                        else -> return false
                    }
                }

                return found
            }

            for (i in 1..lastIndex)
                if (matches(subList(0, i).reversed(), subList(i, size)))
                    return i
            return 0
        }

        return input
            .chunkedBy { it.isEmpty() }
            .sumOf { it.reflectionIndex() * 100L + it.transposed().reflectionIndex() }
    }

    fun part1(input: List<String>): Long = compute(input, true)
    fun part2(input: List<String>): Long = compute(input, false)

    val input = readLines("Day13")
    val inputTest = readLines("Day13-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input)) // 35538
    println(part2(input)) // 30442
}
