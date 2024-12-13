
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.mapLongs

private fun String.split(): List<Long> {
    val half = length / 2
    return listOf(substring(0, half).toLong(), substring(half, length).toLong())
}

private fun blink(stone: Long): List<Long> {
    return buildList {
        if (stone == 0L)
            add(1)
        else if (stone.toString().length % 2 == 0)
            addAll(stone.toString().split())
        else
            add(stone * 2024)
    }
}

fun main() {
    Logger.debug = true
    fun compute(input: String, blinks: Int): Long {
        val cache = mutableMapOf<Pair<Long, Int>, Long>()
        fun simulate(stone: Long, remBlinks: Int): Long {
            if (remBlinks == 0) return 1
            return cache.getOrPut(stone to remBlinks) {
                blink(stone).sumOf { simulate(it, remBlinks - 1) }
            }
        }

        return input.mapLongs(spaceRegex).sumOf { simulate(it, blinks) }
    }

    fun part1(input: String, blinks: Int): Long = compute(input, blinks)
    fun part2(input: String, blinks: Int): Long = compute(input, blinks)

    val input = readText("Day11")

    println(part1(input, 25))
    println(part2(input, 75))
}
