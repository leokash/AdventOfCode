
@file:Suppress("all")

import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.geometry.*
import kotlin.math.abs

private const val PART_ONE_EXPECTED = 26
private const val PART_TWO_EXPECTED = 56_000_011L
private const val PART_TWO_MULTIPLIER = 4_000_000L

private fun neighbors(sensor: Point<Long>, distance: Long, yTarget: Long): List<Point<Long>> {
    val newDist = distance - abs(yTarget - sensor.y)
    return if (newDist < 0) emptyList() else buildList {
        for (x in (sensor.x - newDist)..(sensor.x + newDist)) {
            add(Point(x, yTarget))
        }
    }
}
private fun neighborsOutsideRadius(sensor: Point<Long>, distance: Int, bounds: LongRange): List<Point<Long>> {
    val north = sensor.move(distance + 1, Direction.NORTH)
    val east = sensor.move(distance + 1, Direction.EAST)
    val west = sensor.move(distance + 1, Direction.WEST)
    val south = sensor.move(distance + 1, Direction.SOUTH)
    return (north.lineTo(east) + east.lineTo(south) + south.lineTo(west) + west.lineTo(north)).filter { p ->
        p in bounds && (sensor.manhattanDistance(p)) > distance
    }
}

fun main() {
    Logger.debug = false
    fun computeOne(input: List<String>, targetY: Long): Int {
        val entries = parse(input)
        val sensors = entries.map { it.sensor }.toSet()
        val beacons = entries.map { it.beacon }.toSet()

        return entries
            .flatMap { (s, _, d) -> neighbors(s, d, targetY) }
            .toSet()
            .count { it !in sensors && it !in beacons }
    }
    fun computeTwo(input: List<String>, range: LongRange): Long {
        val sensors =  parse(input).map { (s, _, d) -> s to d }
        return sensors
            .firstNotNullOfOrNull { (sensor, dist) ->
            neighborsOutsideRadius(sensor, dist.toInt(), range)
                .toSet()
                .firstOrNull { p -> sensors.none { (s, d) -> (s.manhattanDistance(p)) <= d } }
            }
            ?.let { (x, y) -> x * PART_TWO_MULTIPLIER + y }
            ?: 0
    }

    fun part1(input: List<String>, yPos: Long): Int = computeOne(input, yPos)
    fun part2(input: List<String>, range: LongRange): Long = computeTwo(input, range)

    val input = readLines("Day15")
    val inputTest = readLines("Day15-Test")

    check(part1(inputTest, 10) == PART_ONE_EXPECTED)
    println(part1(input, 2_000_000))

    check(part2(inputTest, 0..20L) == PART_TWO_EXPECTED)
    println(part2(input, 0..4_000_000L))
}

private val rgx = Regex(""".*=(-?\d+).*=(-?\d+).*=(-?\d+).*=(-?\d+)""")

private data class Entry(val sensor: Point<Long>, val beacon: Point<Long>, val distance: Long)

private fun parse(input: List<String>): List<Entry> {
    return input.map { string ->
        string
            .matchingGroups(rgx)
            .map { it.toLong() }
            .let { (sx, sy, bx, by) -> Entry(Point(sx, sy), Point(bx, by), abs(sx - bx) + abs(sy - by)) }
    }
}
