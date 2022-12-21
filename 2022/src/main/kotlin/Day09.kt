
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.geometry.points.ints.Point

private const val PART_ONE_EXPECTED = 13
private const val PART_TWO_EXPECTED = 36

private typealias CMD = Pair<Direction, Int>

private class Knot(val name: Char, var position: Point) {
    fun follow(head: Point) {
        val dir = position.direction(head) ?: return
        position = when (position mDist head) {
            in 0..1 -> position
            2 -> if (dir in Direction.cardinals) position.move(direction = dir) else position
            else -> if (dir in Direction.ordinals) position.move(direction = dir) else position
        }
    }
    fun move(direction: Direction) {
        position = position.move(direction = direction)
    }
}

fun main() {
    Logger.debug = false
    fun compute(input: List<CMD>, track: Char, tails: List<Char>): Int {
        val start = Point(0, 0)
        val links = ('H' + tails).map { Knot(it, start) }

        val visited = mutableSetOf(start)
        for (cmd in input) {
            repeat(cmd.second) {
                links[0].move(direction = cmd.first)
                for (i in (1 until links.size))
                    with(links[i]) {
                        follow(links[i - 1].position)
                        if (track == name) visited.add(position)
                    }
            }
        }

        return visited.count()
    }
    fun part1(input: List<String>): Int = compute(input.parse(), '1', listOf('1'))
    fun part2(input: List<String>): Int = compute(input.parse(), '9', ('1'..'9').toList())

    val input = readLines("Day09")
    val test1 = readLines("Day09-Test-1")
    val test2 = readLines("Day09-Test-2")

    check(part1(test1) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(test2) == PART_TWO_EXPECTED)
    println(part2(input))
}

private fun List<String>.parse(): List<CMD> {
    return map { string ->
        val (dir, moves) = string.split(" ")
        when (dir) {
            "D" -> Direction.SOUTH to moves.toInt()
            "L" -> Direction.WEST to moves.toInt()
            "R" -> Direction.EAST to moves.toInt()
            "U" -> Direction.NORTH to moves.toInt()
            else -> error("invalid direction provided: ")
        }
    }
}
