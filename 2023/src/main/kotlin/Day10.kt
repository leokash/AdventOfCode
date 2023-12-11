
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.*
import com.github.leokash.adventofcode.utils.graphs.PathFinding
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.next
import kotlin.math.absoluteValue

fun Char.nextDirection(current: Direction): Direction? = when (this) {
    '-' -> current
    '|' -> current
    'J' -> current.rotate(if (current == Direction.SOUTH || current == Direction.WEST) 90 else -90)
    'L' -> current.rotate(if (current == Direction.NORTH || current == Direction.WEST) 90 else -90)
    '7' -> current.rotate(if (current == Direction.SOUTH || current == Direction.EAST) 90 else -90)
    'F' -> current.rotate(if (current == Direction.NORTH || current == Direction.EAST) 90 else -90)

    else -> null
}

fun main() {
    testSample(readLines("Day10-Test-1"), true)
    println(compute(readLines("Day10"), true))

    testSample(readLines("Day10-Test-2"), false)
    println(compute(readLines("Day10"), false))
}

private fun testSample(input: List<String>, partOne: Boolean) {
    input
        .chunkedBy { it.isBlank() }
        .onEach { lines ->
            val (expect, data) = lines[0].findAll("\\d+".toRegex()).first().toInt() to lines.drop(1)
            val result = compute(data, partOne)
            check(result == expect) { "\nExpected: $expect, got: $result... Input:\n $data" }
        }
}

private fun compute(input: List<String>, partOne: Boolean): Int {
    data class Node(val char: Char?, val loc: Point<Int>, val dir: Direction?)

    val bfs = PathFinding.Mode.BFS<Node> { (_, p, d) ->
        val dir = d ?: return@BFS emptyList()
        val np = p.next(dir) ?: return@BFS emptyList()
        input.getOrNull(np)?.let { listOf(Node(it, np, it.nextDirection(dir))) } ?: emptyList()
    }

    val s = input.indexOf('S') ?: return -1
    return Direction.cardinals
        .map { Node(null, s, it) }
        .mapNotNull { n -> PathFinding.findShortestPath(n, bfs) { (c, _, _) -> c == 'S' } }
        .maxBy { it.getScore() }
        .let {
            val length = it.getScore() / 2
            // Part 2 inspiration:  https://www.redblobgames.com/articles/visibility/
            if (partOne) length else it.getPath().windowed(2) { (lhs, rhs) ->  (rhs.loc.x - lhs.loc.x) * lhs.loc.y }.sum().absoluteValue - length + 1
        }
}
