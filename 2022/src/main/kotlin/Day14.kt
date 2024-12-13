
@file:Suppress("all")

import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.geometry.*
import com.github.leokash.adventofcode.utils.math.makeEven
import com.github.leokash.adventofcode.utils.matrix.Matrix
import com.github.leokash.adventofcode.utils.matrix.MatrixStringifier
import com.github.leokash.adventofcode.utils.matrix.contains
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

private const val PART_ONE_EXPECTED = 24
private const val PART_TWO_EXPECTED = 93

fun main() {
    Logger.debug = false
    fun compute(input: List<String>, blockSource: Boolean = false): Int {
        var sand = 0
        val cave = Cave.from(input, blockSource)
        while (cave.simulate())
            sand++

        cave.debug()
        return sand
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    val input = readLines("Day14")
    val inputTest = readLines("Day14-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}

private sealed class Tile {
    object Air: Tile()
    object Rock: Tile()
    object Sand: Tile()
    object Source: Tile()

    val isOccupied: Boolean get() {
        return this is Sand || this is Rock
    }

    override fun toString(): String {
        return when(this) {
            is Air -> "."
            is Rock -> "#"
            is Sand -> "o"
            is Source -> "+"
        }
    }
}

private class Cave (
    rows: Int,
    offset: Int,
    columns: Int,
    sourcePoint: Point<Int>,
    rocksList: Iterable<Point<Int>>
) {
    private val source: Point<Int> = sourcePoint
    private val layout: CaveMatrix = CaveMatrix(rows, columns) { _, _ -> Tile.Air }

    private val down: Direction = Direction.SOUTH
    private val downLeft: Direction = Direction.SOUTH_WEST
    private val downRight: Direction = Direction.SOUTH_EAST

    init {
        for ((x, y) in rocksList)
            layout[x, abs(offset - y)] = Tile.Rock
        layout[source] = Tile.Source
    }

    fun debug() {
        log { MatrixStringifier.stringify(layout) }
    }

    fun simulate(): Boolean {
        var pos: Point<Int>? = source
        while (true) {
            if (pos == null || layout[source] == Tile.Sand)
                return false

            val tiles = pos.let { sp ->
                buildList {
                    (sp + down.point).let { s -> if (layout.contains(s)) add(Triple(down, s, layout[s])) }
                    (sp + downLeft.point).let { sw -> if (layout.contains(sw)) add(Triple(downLeft, sw, layout[sw])) }
                    (sp + downRight.point).let { se -> if (layout.contains(se)) add(Triple(downRight, se, layout[se])) }
                }
            }

            if (tiles.all { (_, _, t) -> t.isOccupied } && tiles.size == 3) {
                layout[pos] = Tile.Sand
                return true
            }

            val south = tiles.firstOrNull { (d, _, _) -> d == down }
            if (south != null && !south.third.isOccupied) { pos = south.second; continue }

            val southWest = tiles.firstOrNull { (d, _, _) -> d == downLeft }
            if (southWest != null && !southWest.third.isOccupied) { pos = southWest.second; continue }

            val southEast = tiles.firstOrNull { (d, _, _) -> d == downRight }
            if (southEast != null && !southEast.third.isOccupied) { pos = southEast.second; continue }
            pos = south?.second
        }
    }

    companion object {
        private const val SOURCE_Y = 500
        fun from(input: List<String>, blockSource: Boolean): Cave {
            var r = 0
            val c = Point(Int.MAX_VALUE, 0)
            val list = mutableSetOf<Point<Int>>()
            for (string in input) {
                val parts = string
                    .split(" -> ")
                    .map { it.split(",").let { (y, x) -> Point(x.toInt(), y.toInt()) } }
                    .onEach { r = max(r, it.x); c.x = min(c.x, it.y); c.y = max(c.y, it.y) }
                    .zipWithNext()
                    .flatMap { (lhs, rhs) -> computeLine(lhs, rhs) }
                list.addAll(parts)
            }

            if (blockSource) {
                return caveWithBottomFloor(r, list)
            }
            return Cave(r + 1, c.x, (c.y - c.x) + 1, Point(0, SOURCE_Y - c.x), list)
        }

        private fun caveWithBottomFloor(rows: Int, points: Set<Point<Int>>): Cave {
            val nRows = rows + 3
            val columns = (0 until nRows).fold(1) { acc, _ -> acc + 2 }.makeEven() + 2
            val tilesWithFloor = points + (0 until (columns + 1)).map { i ->
                Point(nRows - 1, (SOURCE_Y - (columns / 2)) + i)
            }

            return Cave(nRows, SOURCE_Y - (columns / 2), columns + 1, Point(0, (columns / 2)), tilesWithFloor)
        }

        private fun computeLine(lhs: Point<Int>, rhs: Point<Int>): List<Point<Int>> {
            return buildList {
                for (x in (min(lhs.x, rhs.x)..(max(lhs.x, rhs.x))))
                    for (y in (min(lhs.y, rhs.y)..(max(lhs.y, rhs.y))))
                        add(Point(x, y))
            }
        }
    }
}

private typealias CaveMatrix = Matrix<Tile>

