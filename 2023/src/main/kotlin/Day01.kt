
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 142
private const val PART_TWO_EXPECTED = 281

private val map: Map<String, Int> = mutableMapOf(
    "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
)

fun main() {
    Logger.debug = true
    fun part1(input: List<String>): Int = input.sumOf { string ->
        string
            .mapNotNull { it.digitToIntOrNull() }
            .let { (it.first() * 10) + it.last() }
    }

    fun part2(input: List<String>): Int = input.sumOf { string ->
        string.mapIndexedNotNull { i, c ->
            c.digitToIntOrNull() ?: map.keys
                .filter { string[i] == it.first() }
                .firstOrNull { string[i..(i + it.length)] == it }
                ?.let { map.getValue(it) }
        }.let { (it.first() * 10) + it.last() }
    }

    val input = readLines("Day01")
    
    check(part1(readLines("Day01-Test")) == PART_ONE_EXPECTED)
    check(part2(readLines("Day01-Test-2")) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
