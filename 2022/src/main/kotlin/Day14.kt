
@file:Suppress("all")

import matrix.*
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

private const val PART_ONE_EXPECTED = 24
private const val PART_TWO_EXPECTED = 93

fun main() {
    Logger.debug = true
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
    sourcePoint: Point,
    rocksList: Iterable<Point>
) {
    private val source: Point = sourcePoint
    private val layout: CaveMatrix = CaveMatrix(rows, columns)

    private val down: Direction = Direction.SOUTH
    private val downLeft: Direction = Direction.SOUTH_WEST
    private val downRight: Direction = Direction.SOUTH_EAST
    private val directions: List<Direction> = listOf(down, downLeft, downRight)

    init {
        for ((x, y) in rocksList)
            layout[x, abs(offset - y)] = Tile.Rock
        layout[source] = Tile.Source
    }

    fun debug() {
        log { MatrixStringifier().stringify(layout) }
    }
    fun simulate(): Boolean {
        var pos: Point? = source
        while (true) {
            if (pos == null || layout[source] == Tile.Sand)
                return false

            val tiles = pos.let { sp ->
                layout
                    .neighbors(sp, true)
                    .mapNotNull { (p, t) -> val d = sp.direction(p); if (d != null) Triple(d, p, t) else null }
                    .filter { (d, _, _) -> d in directions }
            }

            if (tiles.all { (_, _, t) -> t.isOccupied } && tiles.size == 3) {
                layout[pos] = Tile.Sand
                return true
            }

            val south = tiles.firstOrNull { (d, _, _) -> d == down }
            if (south != null && !south.third.isOccupied) { pos = south.second; continue }

            val southEast = tiles.firstOrNull { (d, _, _) -> d == downRight }
            if (southEast != null && !southEast.third.isOccupied) { pos = southEast.second; continue }

            val southWest = tiles.firstOrNull { (d, _, _) -> d == downLeft }
            if (southWest != null && !southWest.third.isOccupied) { pos = southWest.second; continue }
            pos = south?.second
        }
    }

    companion object {
        private const val sourceY = 500
        fun from(input: List<String>, blockSource: Boolean): Cave {
            var r = 0
            val c = Point(Int.MAX_VALUE, 0)
            val list = mutableSetOf<Point>()
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
            return Cave(r + 1, c.x, (c.y - c.x) + 1, Point(0, sourceY - c.x), list)
        }

        private fun caveWithBottomFloor(rows: Int, points: Set<Point>): Cave {
            val nRows = rows + 3
            val columns = (0 until nRows).fold(1) { acc, _ -> acc + 2 }.makeEven() + 2
            val tilesWithFloor = points + (0 until (columns + 1)).map { i ->
                Point(nRows - 1, (sourceY - (columns / 2)) + i)
            }

            return Cave(nRows, sourceY - (columns / 2), columns + 1, Point(0, (columns / 2)), tilesWithFloor)
        }

        private fun computeLine(lhs: Point, rhs: Point): List<Point> {
            return buildList {
                for (x in (min(lhs.x, rhs.x)..(max(lhs.x, rhs.x))))
                    for (y in (min(lhs.y, rhs.y)..(max(lhs.y, rhs.y))))
                        add(Point(x, y))
            }
        }
    }
}

private class CaveMatrix(
    override val rows: Int,
    override val columns: Int,
    init: (Int, Int) -> Tile = { _, _ ->  Tile.Air }
): Matrix<Tile>() {
    override val store: Array<Array<Tile>> = Array(rows) { x ->
        Array(columns) { y ->
            init(x, y)
        }
    }
}
