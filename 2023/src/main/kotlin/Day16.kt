
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

            fun branch() {
                with(dir.rotate(90)) { beam.next(this)?.let { simulate(it, this, energised, visited) } }
                with(dir.rotate(-90)) { beam.next(this)?.let { simulate(it, this, energised, visited) } }
            }
            fun carryOn() {
                beam.next(dir)?.let { simulate(it, dir, energised, visited) }
            }

            when (input[beam]) {
                '/' -> when (dir) {
                    Direction.EAST, Direction.WEST -> with(dir.rotate(-90)) { beam.next(this)?.let { simulate(it, this, energised, visited) } }
                    Direction.NORTH, Direction.SOUTH -> with(dir.rotate(90)) { beam.next(this)?.let { simulate(it, this, energised, visited) } }

                    else -> error("only moving in cardinal directions")
                }
                '\\' -> when (dir) {
                    Direction.EAST, Direction.WEST -> with(dir.rotate(90)) { beam.next(this)?.let { simulate(it, this, energised, visited) } }
                    Direction.NORTH, Direction.SOUTH -> with(dir.rotate(-90)) { beam.next(this)?.let { simulate(it, this, energised, visited) } }
                    else -> error("only moving in cardinal directions")
                }
                '|' -> if (dir in vsDirs) branch() else carryOn()
                '-' -> if (dir in hsDirs) branch() else carryOn()
                else -> carryOn()
            }
        }

        return startingPoints.maxOf { (beam, dir) ->
            mutableSetOf<Point<Int>>()
                .apply { simulate(beam, dir, this, mutableSetOf()) }
                .size
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
