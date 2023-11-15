
import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.math.geometry.*
import com.github.leokash.adventofcode.utils.readLines

private val String.asDirection: Direction
    get() {
    return when (this) {
        "up" -> Direction.NORTH
        "down" -> Direction.SOUTH
        "forward" -> Direction.EAST
        else -> throw RuntimeException("Invalid direction given: $this")
    }
}
private fun List<String>.parse(): List<Pair<Direction, Int>> {
    return map { string ->
        string.split(' ').let { arr ->
            arr[0].trim().asDirection to arr[1].trim().toInt()
        }
    }
}
private fun Point<Int>.update(moves: Int, direction: Direction) {
    when(direction) {
        Direction.EAST -> x += moves
        Direction.NORTH -> y -= moves
        Direction.SOUTH -> y += moves
        else -> Unit
    }
}
private data class Vector3(var aim: Int, var x: Int, var y: Int) {
    fun update(moves: Int, direction: Direction) {
        when(direction) {
            Direction.EAST -> { x += moves; y += (aim * moves) }
            Direction.NORTH -> aim -= moves
            Direction.SOUTH -> aim += moves
            else -> Unit
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .parse()
            .fold(Point(0, 0)) { point, (dir, moves) -> point.apply { update(moves, dir) } }
            .let { it.x * it.y }
    }
    fun part2(input: List<String>): Int {
        return input
            .parse()
            .fold(Vector3(0,0,0)) { point, (dir, moves) -> point.apply { update(moves, dir) } }
            .let { it.x * it.y }
    }

    val input = readLines("Day02")
    val inputTest = readLines("Day02-Test")

    check(part1(inputTest) == 150)
    check(part2(inputTest) == 900)

    println(part1(input))
    println(part2(input))
}
