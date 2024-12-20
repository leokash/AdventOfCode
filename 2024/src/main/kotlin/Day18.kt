
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.graphs.PathFinding
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.neighbors
import com.github.leokash.adventofcode.utils.matrix.CharMatrix
import com.github.leokash.adventofcode.utils.matrix.contains
import com.github.leokash.adventofcode.utils.matrix.MatrixStringifier

private const val PART_ONE_EXPECTED = 22
private val part_two_expected = Point(6,1)

fun main() {
    Logger.enabled = true
    fun parse(input: List<String>, testing: Boolean): Pair<IntRange, List<IntPoint>> {
        return 0..(if (testing) 6 else 70) to input.map { it.split(commaRegex).let { IntPoint(it[0].toInt(), it[1].toInt()) } }
    }

    fun part1(input: List<String>, testing: Boolean): Int {
        val (range, data) = parse(input, testing)
        val corrupted = data.take(if (testing) 12 else 1024)
        val bfsSearchMode = PathFinding.Mode.BFS { p: IntPoint -> p.neighbors { (_, p0) -> p0 in range && p0 !in corrupted }.map { it.second } }

        return PathFinding.findShortestPath(Point(0, 0), Point(range.last, range.last), bfsSearchMode)?.cost ?: 0
    }
    fun part2(input: List<String>, testing: Boolean): IntPoint {
        var size = if (testing) 12 else 1024
        val (range, data) = parse(input, testing)

        val start = Point(0, 0)
        val finish = Point(range.last, range.last)
        while (true) {
            size++
            val cor = data.take(size)
            val bfs = PathFinding.Mode.BFS { p: IntPoint -> p.neighbors { (_, p0) -> p0 in range && p0 !in cor }.map { it.second } }
            if (PathFinding.findShortestPath(start, finish, bfs) == null)
                return cor.last()
        }

        return Point(0, 0)
    }

    val input = readLines("Day18")
    val inputTest = readLines("Day18-Test")

    check(part1(inputTest, true) == PART_ONE_EXPECTED)
    println(part1(input, false))

    check(part2(inputTest, true).log { "$this" } == part_two_expected)
    println(part2(input, false))
}
