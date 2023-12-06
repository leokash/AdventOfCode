
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.productIndexed

private const val PART_ONE_EXPECTED = 288
private const val PART_TWO_EXPECTED = 71503

private val spaceRgx = """\s+""".toRegex()

fun main() {
    Logger.debug = true
    fun compute(input: List<String>, partOne: Boolean): Int {
        val times = input[0].substringAfter("Time: ").trim().split(spaceRgx).map { it.toLong() }
        val distances = input[1].substringAfter("Distance: ").trim().split(spaceRgx).map { it.toLong() }

        fun simulate(time: Long, goal: Long): Int {
            var count = 0
            for (i in 0..< time)
                if ((time - i) * i > goal)
                    count++

            return count
        }

        if (partOne) {
            return times.productIndexed { idx, time -> simulate(time, distances[idx]) }
        }

        return simulate(times.joinToString("").toLong(), goal = distances.joinToString("").toLong())
    }

    fun part1(input: List<String>): Int = compute(input, true)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day06")
    val inputTest = readLines("Day06-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
