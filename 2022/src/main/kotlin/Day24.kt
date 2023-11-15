
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.isEdge
import com.github.leokash.adventofcode.utils.math.geometry.*
import kotlin.math.min

private const val PART_ONE_EXPECTED = 18
private const val PART_TWO_EXPECTED = 54

fun main() {
    Logger.debug = false
    data class State(val time: Int, val location: Point<Int>)
    fun findShortestTime(start: Point<Int>, goal: Point<Int>, valley: Valley, startTime: Int = 0): Int {
        fun neighbors(time: Int, current: Point<Int>): List<Point<Int>> {
            return current.neighbors().mapNotNull { (_, p) ->
                if (p == start || p == goal || valley.vacant(p, time)) p else null
            }
        }

        var best = Int.MAX_VALUE
        val seen = mutableSetOf<State>()
        val queue = ArrayDeque<State> ().apply { add(State(startTime, start)) }

        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            when {
                state in seen -> continue
                state.time > best -> continue
                state.location == goal -> best = min(best, state.time)
                else -> {
                    seen.add(state)
                    val (time, current) = state
                    valley.moveBlizzards(time)
                    neighbors(time, current).onEach { queue.add(State(time + 1, it)) }
                    if (current == start || current == goal || valley.vacant(current, time)) queue.add(State(time + 1, current))
                }
            }
        }

        return best
    }

    fun part1(input: List<String>): Int {
        return parse(input).let { (start, goal, valley) -> findShortestTime(start, goal, valley) }
    }
    fun part2(input: List<String>): Int {
        val (start, goal, valley) = parse(input)
        return findShortestTime(start, goal, valley, findShortestTime(goal, start, valley, findShortestTime(start, goal, valley)))
    }

    val input = readLines("Day24")
    val sample = readLines("Day24-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}

private val Char.direction: Direction get() {
    return when(this) {
        '>' -> Direction.EAST
        '<' -> Direction.WEST
        '^' -> Direction.NORTH
        'v' -> Direction.SOUTH
        else -> error("Unable to convert to direction, invalid char provided: $this")
    }
}
private class Blizzard(var x: Int, var y: Int, val direction: Direction)
private class Valley(val xBounds: IntRange, val yBounds: IntRange, private val blizzards: List<Blizzard>) {
    private val cachedBlizzard = mutableMapOf<Int, List<Blizzard>>()

    fun moveBlizzards(time: Int) {
        if (cachedBlizzard[time] != null) return
        val initial = cachedBlizzard[time - 1] ?: blizzards
        cachedBlizzard[time] = initial.map { b ->
            val next = with(Point(b.x, b.y) + b.direction.point) {
                if (!isEdge(this)) this else when {
                    x == xBounds.last -> Point(xBounds.first + 1, y)
                    y == yBounds.last -> Point(x, yBounds.first + 1)
                    x == xBounds.first -> Point(xBounds.last - 1, y)
                    else -> Point(x, yBounds.last - 1)
                }
            }

            Blizzard(next.x, next.y, b.direction)
        }
    }
    fun isEdge(point: Point<Int>): Boolean {
        return point.x isEdge xBounds || point.y isEdge yBounds
    }
    fun vacant(point: Point<Int>, time: Int): Boolean {
        val locations = cachedBlizzard.getValue(time)
        return !isEdge(point) &&
                point.x in xBounds &&
                point.y in yBounds &&
                locations.none { it.x == point.x && it.y == point.y }
    }
}

private fun parse(input: List<String>): Triple<Point<Int>, Point<Int>, Valley> {
    return Triple(
        Point(0, 1),
        Point(input.lastIndex, input[0].lastIndex - 1),
        Valley(0..(input.lastIndex), 0..(input[0].lastIndex), input.flatMapIndexed { x, row ->
        row.mapIndexedNotNull { y, c ->
            if (c == '.' || c == '#') null else Blizzard(x, y, c.direction)
        }
    }))
}
