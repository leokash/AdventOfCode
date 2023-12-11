
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.indicesOf
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.manhattanDistance

private const val PART_ONE_EXPECTED = 374L
private const val PART_TWO_EXPECTED_BY_10 = 1030L
private const val PART_TWO_EXPECTED_BY_100 = 8410L

fun main() {
    fun compute(input: List<String>, dist: Int): Long {
        val emptyExes = input.mapIndexedNotNull { i, s -> if (s.any { it == '#' }) null else i }
        val emptyWhys = (0..input.lastIndex).mapNotNull { i -> if (input.any { it[i] == '#' }) null else i }

        val indices = input.indicesOf('#').map { (x, y) ->
            Point(
                x + ((dist - 1) * emptyExes.takeWhile { it < x }.size),
                y + ((dist - 1) * emptyWhys.takeWhile { it < y }.size)
            )
        }

        var sum = 0L
        for (i in 0..indices.lastIndex)
            for (j in (i+1)..indices.lastIndex)
                sum += indices[i].manhattanDistance(indices[j])

        return sum
    }

    val input = readLines("Day11")
    val inputTest = readLines("Day11-Test")

    check(compute(inputTest, 2) == PART_ONE_EXPECTED)
    check(compute(inputTest, 10) == PART_TWO_EXPECTED_BY_10)
    check(compute(inputTest, 100) == PART_TWO_EXPECTED_BY_100)

    println(compute(input, 2))
    println(compute(input, 1_000_000))
}
