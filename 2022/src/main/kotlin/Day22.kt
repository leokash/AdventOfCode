
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.geometry.points.ints.Point
import com.github.leokash.adventofcode.utils.matrix.*

private const val PART_ONE_EXPECTED = 6032
private const val PART_TWO_EXPECTED = 5031

private sealed class Move {
    companion object {
        fun from(s: String): Move {
            return when (s) {
                "R" -> Clockwise
                "L" -> CounterClockwise
                else -> Forward(s.toInt())
            }
        }
    }
}
private object Clockwise: Move()
private object CounterClockwise: Move()
private data class Forward(val steps: Int): Move()

private val Direction.facing: Int get() {
    return when (this) {
        Direction.EAST  -> 0
        Direction.SOUTH -> 1
        Direction.WEST  -> 2
        Direction.NORTH -> 3
        else -> error("only interested in cardinals!")
    }
}
private fun Direction.moveClockwise(): Direction { // 90°
    val cardinals = Direction.cardinals
    return cardinals[(cardinals.indexOf(this) + 1) % cardinals.size]
}
private fun Direction.moveCounterClockwise(): Direction { // -90°
    val cardinals = Direction.cardinals
    return cardinals[((cardinals.indexOf(this) - 1) % cardinals.size).let { if (it < 0) it + cardinals.size else it }]
}

private sealed class MoveResult
private object Null: MoveResult()
private object Path: MoveResult()
private object Void: MoveResult()
private object Wall: MoveResult()

private data class Cube(val size: Int, private val isHorizontal: Boolean, private val builder: Builder.() -> Unit) {
    private val faces = mutableMapOf<Int, Builder.Facing>()
    private val facesCache = mutableMapOf<Point, Builder.Facing>()
    private val xBounds = IntRange(0, (if (isHorizontal) size * 3 else size * 4) - 1)
    private val yBounds = IntRange(0, (if (isHorizontal) size * 4 else size * 3) - 1)

    init {
        builder(Builder())
    }

    inner class Builder {
        inner class Facing (val id: Int, val position: Point, val destinations: Map<Direction, Pair<Int, Direction>>) {
            private val xBounds = IntRange(position.x, position.x + (size - 1))
            private val yBounds = IntRange(position.y, position.y + (size - 1))

            operator fun contains(point: Point): Boolean {
                return point.x in xBounds && point.y in yBounds
            }
            fun translate(point: Point, src: Direction): Triple<Facing, Point, Direction> {
                return destinations.getValue(src).let { (i, dest) ->
                    faces.getValue(i).let { Triple(it, point.translate(src, dest, it), dest) }
                }
            }

            private fun Point.relativeX() = x - position.x
            private fun Point.relativeY() = y - position.y
            private fun Point.fRelativeX() = (xBounds.last - (x - position.x)) % size
            private fun Point.fRelativeY() = (yBounds.last - (y - position.y)) % size

            private fun Point.translate(src: Direction, dest: Direction, other: Facing): Point {
                return when (src to dest) {
                    Pair(Direction.NORTH, Direction.NORTH) -> Point(other.xBounds.last, other.position.y + relativeY())
                    Pair(Direction.NORTH, Direction.EAST)  -> Point(other.position.x + relativeY(), other.position.y)
                    Pair(Direction.EAST,  Direction.NORTH) -> Point(other.xBounds.last, other.position.y + relativeX())
                    Pair(Direction.EAST,  Direction.EAST)  -> Point(other.position.x + relativeX(), other.position.y)
                    Pair(Direction.EAST,  Direction.SOUTH) -> Point(other.position.x, other.position.y + fRelativeX())
                    Pair(Direction.EAST,  Direction.WEST)  -> Point(other.position.x + fRelativeX(), other.yBounds.last)
                    Pair(Direction.SOUTH, Direction.NORTH) -> Point(other.xBounds.last, other.position.y + fRelativeY())
                    Pair(Direction.SOUTH, Direction.SOUTH) -> Point(other.position.x, other.position.y + relativeY())
                    Pair(Direction.SOUTH, Direction.WEST)  -> Point(other.position.x + relativeY(), other.yBounds.last)
                    Pair(Direction.WEST,  Direction.WEST)  -> Point(other.position.x + relativeX(), other.yBounds.last)
                    Pair(Direction.WEST,  Direction.EAST)  -> Point(other.position.x + fRelativeX(), other.position.y)
                    Pair(Direction.WEST,  Direction.SOUTH) -> Point(other.position.x, other.position.y + relativeX())
                    else -> error("\n\nInvalid state:($src => $dest)\nmoving from $this, source ${this@Facing}\ndestination $other\n")
                }
            }

            override fun toString(): String {
                return "(id=$id, position=$position, destinations=$destinations)"
            }
        }

        fun facing(src: Int, location: Int, destinations: List<Triple<Int, Int, Int>>) {
            val x = location / if (isHorizontal) 4 else 3
            val y = location % if (isHorizontal) 4 else 3
            faces[src] = Facing(src, Point(x * size, y * size), destinations.associate { (a, b, c) ->
                //a == outward direction from this facing, b == destination facing id, c == destination translated direction
                Direction.cardinals[a] to Pair(b, Direction.cardinals[c])
            })
        }
    }

    private fun findFacing(point: Point): Builder.Facing {
        return facesCache.getOrPut(point) {
            faces.values.first { point in it }
        }
    }
    private operator fun Point.plus(dir: Direction) = this + dir.point

    private fun move2d(start: Point, dir: Direction, steps: Int, predicate: (Point) -> MoveResult): Pair<Point, Direction> {
        fun wrap(s: Point): Point {
            var tmp = s
            while (true) {
                tmp = when (predicate(tmp)) {
                    Void -> tmp + dir
                    Path -> return tmp
                    Wall -> return tmp
                    Null -> {
                        if (tmp.x !in xBounds)
                            Point(if (tmp.x < 0) xBounds.last else 0, tmp.y)
                        else
                            Point(tmp.x, if (tmp.y < 0) yBounds.last else 0)
                    }
                }
            }
        }

        var tp = start
        repeat(steps) {
            val point = wrap(tp + dir)
            if (predicate(point) is Wall) return Pair(tp, dir)
            tp = point
        }

        return Pair(tp, dir)
    }
    private fun move3d(start: Point, dir: Direction, steps: Int, predicate: (Point) -> MoveResult): Pair<Point, Direction> {
        var td = dir
        var tp = start
        var facing = findFacing(start)

        repeat(steps) {
            val next = tp + td
            if (next !in facing) {
                val (f, p, d) = facing.translate(tp, td)
                if (predicate(p) is Wall) return Pair(tp, td)
                td = d; tp = p; facing = f
            } else {
                when (predicate(next)) {
                    Path -> tp = next
                    Wall -> return Pair(tp, td)
                    else -> error("invalid state with point: $next and face: $facing")
                }
            }
        }

        return Pair(tp, td)
    }
    fun move(start: Point, dir: Direction, steps: Int, is3d: Boolean, predicate: (Point) -> MoveResult): Pair<Point, Direction> {
        return when (is3d) {
            true -> move3d(start, dir, steps, predicate)
            else -> move2d(start, dir, steps, predicate)
        }
    }
}

fun main() {
    Logger.debug = false
    fun compute(input: List<String>, cube: Cube, is3d: Boolean = false): Int {
        val (start, moves, world) = parse(input)

        var pos = start
        var dir = Direction.EAST
        val predicate: (Point) -> MoveResult = { p: Point ->
            when (world.getOrNull(p)) {
                '.' -> Path
                '#' -> Wall
                ' ' -> Void
                null -> Null
                else -> error("invalid state found for: $p...")
            }
        }

        moves.forEach { move ->
            when (move) {
                is Forward -> {
                    cube.move(pos, dir, move.steps, is3d, predicate).also { (p, d) ->
                        dir = d; pos = p
                    }
                }
                is Clockwise -> dir = dir.moveClockwise()
                is CounterClockwise -> dir = dir.moveCounterClockwise()
            }
        }

        return ((1000 * (pos.x + 1)) + (4 * (pos.y + 1)) + dir.facing)
    }

    val sCube = Cube(4, true) {
        facing(1,  2, listOf(Triple(0, 2, 2), Triple(1, 6, 3), Triple(2, 4, 2), Triple(3, 3, 2)))
        facing(2,  4, listOf(Triple(0, 1, 2), Triple(1, 3, 1), Triple(2, 5, 0), Triple(3, 6, 0)))
        facing(3,  5, listOf(Triple(0, 1, 1), Triple(1, 4, 1), Triple(2, 5, 1), Triple(3, 2, 3)))
        facing(4,  6, listOf(Triple(0, 1, 0), Triple(1, 6, 2), Triple(2, 5, 2), Triple(3, 3, 3)))
        facing(5, 10, listOf(Triple(0, 4, 0), Triple(1, 6, 1), Triple(2, 2, 0), Triple(3, 3, 0)))
        facing(6, 11, listOf(Triple(0, 4, 3), Triple(1, 1, 3), Triple(2, 2, 1), Triple(3, 5, 3)))
    }
    val iCube = Cube(50, false) {
        facing(1, 1, listOf(Triple(0, 6, 1), Triple(1, 2, 1), Triple(2, 3, 2), Triple(3, 4, 1)))
        facing(2, 2, listOf(Triple(0, 6, 0), Triple(1, 5, 3), Triple(2, 3, 3), Triple(3, 1, 3)))
        facing(3, 4, listOf(Triple(0, 1, 0), Triple(1, 2, 0), Triple(2, 5, 2), Triple(3, 4, 2)))
        facing(4, 6, listOf(Triple(0, 3, 1), Triple(1, 5, 1), Triple(2, 6, 2), Triple(3, 1, 1)))
        facing(5, 7, listOf(Triple(0, 3, 0), Triple(1, 2, 3), Triple(2, 6, 3), Triple(3, 4, 3)))
        facing(6, 9, listOf(Triple(0, 4, 0), Triple(1, 5, 0), Triple(2, 2, 2), Triple(3, 1, 2)))
    }

    fun part1(input: List<String>, cube: Cube) = compute(input, cube)
    fun part2(input: List<String>, cube: Cube) = compute(input, cube, true)

    val input = readLines("Day22")
    val sample = readLines("Day22-Test")

    check(part1(sample, sCube) == PART_ONE_EXPECTED)
    println(part1(input, iCube))

    check(part2(sample, sCube) == PART_TWO_EXPECTED)
    println(part2(input, iCube))
}

private fun parse(input: List<String>): Triple<Point, List<Move>, CharMatrix> {
    fun parseMoves(string: String, container: MutableList<Move>) {
        val rgx = Regex("""[LR]|\d+""")
        container.addAll(rgx.findAll(string).map { Move.from(it.groupValues[0]) })
    }

    val world = input.dropLast(2)
    val maxCol = world.maxOf { it.length }
    return Triple(
        Point(0, input[0].indexOf('.')),
        mutableListOf<Move>().apply { parseMoves(input.last(), this) },
        CharMatrix(world.size, maxCol) { x, y -> world[x].getOrNull(y) ?: ' ' }
    )
}
