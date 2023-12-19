
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.get
import com.github.leokash.adventofcode.utils.collections.contains
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.next

private const val PART_ONE_EXPECTED = 46
private const val PART_TWO_EXPECTED = 51

private val vsDirs = listOf(Direction.EAST, Direction.WEST)
private val hsDirs = listOf(Direction.NORTH, Direction.SOUTH)

private fun List<String>.generateStarts(): List<Pair<Point<Int>, Direction>> {
    val lastX = lastIndex
    val lastY = this[0].lastIndex
    return indices.flatMap { x ->
         buildList {
             add(Point(x, 0) to Direction.EAST)
             add(Point(x, lastY) to Direction.WEST)
         }
    } + this[0].indices.flatMap { y ->
        buildList {
            add(Point(0, y) to Direction.SOUTH)
            add(Point(lastX, y) to Direction.NORTH)
        }
    }
}

fun main() {
    fun compute(input: List<String>, startingPoints: List<Pair<Point<Int>, Direction>> = listOf(Point(0, 0) to Direction.EAST)): Int {
        fun simulate(beam: Point<Int>, dir: Direction, energised: MutableSet<Point<Int>>, visited: MutableSet<Pair<Point<Int>, Direction>>) {
            val pair = beam to dir
            if (beam !in input || pair in visited)
               return

            visited += pair
            energised += beam

            fun rotateAndMove(degrees: Int) {
                val nDir = if (degrees == 0) dir else dir.rotate(degrees)
                beam.next(nDir)?.let { simulate(it, nDir, energised, visited) }
            }

            when (input[beam]) {
                '.' -> rotateAndMove(0)
                '/' -> rotateAndMove(if (dir == Direction.EAST || dir == Direction.WEST) -90 else 90)
                '\\' -> rotateAndMove(if (dir == Direction.EAST || dir == Direction.WEST) 90 else -90)
                '|' -> if (dir !in vsDirs) rotateAndMove(0) else { rotateAndMove(90); rotateAndMove(-90) }
                '-' -> if (dir !in hsDirs) rotateAndMove(0) else { rotateAndMove(90); rotateAndMove(-90) }
            }
        }

        return startingPoints.maxOf { (beam, dir) ->
            mutableSetOf<Point<Int>>().apply { simulate(beam, dir, this, mutableSetOf()) }.size
        }
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, input.generateStarts())

    val input = readLines("Day16")
    val inputTest = readLines("Day16-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
