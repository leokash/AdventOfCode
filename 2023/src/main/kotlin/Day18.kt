
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.area

private const val PART_ONE_EXPECTED = 62L
private const val PART_TWO_EXPECTED = 952408144115L

private val rgx = """([RDLU]) (\d+) \((#[a-f0-9]{6})\)""".toRegex()

private val Char.toDir: Direction get() = when (this) {
    '0' -> Direction.EAST
    '1' -> Direction.SOUTH
    '2' -> Direction.WEST
    '3' -> Direction.NORTH

    else -> error("invalid direction provided: $this")
}
private val String.toDir: Direction get() = when(this) {
    "U" -> Direction.NORTH
    "R" -> Direction.EAST
    "D" -> Direction.SOUTH
    "L" -> Direction.WEST

    else -> error("invalid direction provided: $this")
}

private fun Point<Long>.edge(dir: Direction, length: Long): Point<Long> {
    return when (dir) {
        Direction.EAST -> Point(x + length, y)
        Direction.WEST -> Point(x - length, y)
        Direction.NORTH -> Point(x, y - length)
        Direction.SOUTH -> Point(x, y + length)

        else -> error("only moving in cardinal directions")
    }
}

fun main() {
    Logger.debug = true
    fun compute(input: List<String>, partOne: Boolean): Long {
        fun parse(s: String): Triple<Direction, Long, String> {
            val (dir, steps, colour) =  s.matchingGroups(rgx)
            if (partOne)
                return Triple(dir.toDir, steps.toLong(), colour)
            return colour.substringAfter("#").let { Triple(it.last().toDir, it.dropLast(1).toLong(16), colour) }
        }

        val vertices = mutableListOf(Point(0L, 0L))
        for (string in input) {
            val (dir, steps, _) = parse(string)
            vertices += vertices.last().edge(dir, steps)
        }

        return vertices.area()
    }

    fun part1(input: List<String>): Long = compute(input, true)
    fun part2(input: List<String>): Long = compute(input, false)

    val input = readLines("Day18")
    val inputTest = readLines("Day18-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
