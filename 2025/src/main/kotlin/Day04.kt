
import com.github.leokash.adventofcode.utils.Logger
import com.github.leokash.adventofcode.utils.collections.toCharMatrix
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.matrix.filter
import com.github.leokash.adventofcode.utils.matrix.neighbors
import com.github.leokash.adventofcode.utils.readLines

private const val PART_ONE_EXPECTED = 13
private const val PART_TWO_EXPECTED = 43

fun main() {
    Logger.enabled = false
    fun compute(input: List<String>, partOne: Boolean = true): Int {
        val mat = input.toCharMatrix()
        val points = mutableSetOf<Point<Int>>()
        while (true) {
            val tmp = mat
                .filter { p, c -> c == '@' && mat.neighbors(p, true) { _, _, c0 -> c0 == '@' }.size < 4 }
                .map { it.first }
                .onEach { mat[it]  = '.' }
                .onEach { points.add(it) }

            if (partOne || tmp.isEmpty()) break
        }

        return points.size
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day04")
    val inputTest = readLines("Day04-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
