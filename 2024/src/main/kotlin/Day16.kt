
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.*
import com.github.leokash.adventofcode.utils.graphs.PathFinding
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.neighbors

private val partOneExpectedMap = mapOf("A" to 7036, "B" to 11048)
private val partTwoExpectedMap = mapOf("A" to 45, "B" to 64)

fun main() {
    fun compute(input: List<String>, findBestSeats: Boolean = false): Int {
        val start = input.indexOf('S') ?: return 0
        val finish = input.indexOf('E') ?: return 0
        val greedy = PathFinding.Mode.GREEDY<Pair<Direction, IntPoint>>(
            cost = { (d0, _), (d1, _) -> if (d0 == d1) 1 else 1001 },
            neighbors = { (_, p0) ->  p0.neighbors { (_, p1) -> input[p1] != '#' } }
        )

        val result = PathFinding.findShortestPath(Direction.EAST to start, greedy) { (_, p) -> p == finish } ?: return 0
        if (!findBestSeats) {
            return result.cost
        }

        return PathFinding
            .findPaths(Direction.EAST to start, greedy, result.cost) { (_, p) -> p == finish }
            .flatMap { it.path }
            .map { it.second }
            .toSet()
            .size
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    val input = readLines("Day16")
    for ((fileId, expected) in partOneExpectedMap) {
        val actual = part1(readLines("Day16-Test-$fileId"))
        check(expected == actual) { "--- PART 1 => Failed for file id: $fileId... expected: $expected, actual: $actual"}
    }
    println(part1(input))

    for ((fileId, expected) in partTwoExpectedMap) {
        val actual = part2(readLines("Day16-Test-$fileId"))
        check(expected == actual) { "--- PART 2 => Failed for file id: $fileId... expected: $expected, actual: $actual"}
    }
    println(part2(input))
}
