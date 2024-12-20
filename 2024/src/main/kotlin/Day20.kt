
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.get
import com.github.leokash.adventofcode.utils.collections.indexOf
import com.github.leokash.adventofcode.utils.graphs.PathFinding
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.manhattanDistance
import com.github.leokash.adventofcode.utils.math.geometry.neighbors

private const val PART_ONE_EXPECTED = 44
private const val PART_TWO_EXPECTED = 285

fun main() {
    Logger.enabled = true
    fun compute(input: List<String>, cheatTime: Int = 2, savingGoal: Int): Int {
        val s = input.indexOf('S') ?: return 0
        val e = input.indexOf('E') ?: return 0
        val bfs = PathFinding.Mode.BFS<IntPoint> { p ->
            p.neighbors { (_, p0) -> input[p0] != '#' }.map { (_, p0) -> p0 }
        }

        val path = PathFinding.findShortestPath(s, bfs) { p -> p == e }?.path ?: return 0
        var count = 0
        for (i in 0..path.lastIndex) {
            count += ((i + savingGoal)..path.lastIndex).count { j ->
                val dist = path[i].manhattanDistance(path[j])
                dist <= cheatTime && dist <= j - i - savingGoal
            }
        }

        return count
    }

    fun part1(input: List<String>, savingGoal: Int): Int = compute(input, 2, savingGoal)
    fun part2(input: List<String>, savingGoal: Int): Int = compute(input, 20, savingGoal)

    val input = readLines("Day20")
    val inputTest = readLines("Day20-Test")

    check(part1(inputTest, 2) == PART_ONE_EXPECTED)
    println(part1(input, 100))

    check(part2(inputTest, 50) == PART_TWO_EXPECTED)
    println(part2(input, 100))
}
