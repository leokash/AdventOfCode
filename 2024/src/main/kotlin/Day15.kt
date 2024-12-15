
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.move
import com.github.leokash.adventofcode.utils.matrix.Matrix
import com.github.leokash.adventofcode.utils.matrix.stringsToMatrix

private val partOneExpectedMap = mapOf("A" to 2028, "B" to 10092)
private val partTwoExpectedMap = mapOf("B" to 9021, "C" to 618)

private typealias Warehouse = Matrix<Tile>

private sealed class Tile {
    data object Box: Tile()
    data object LBox: Tile()
    data object RBox: Tile()
    data object Free: Tile()
    data object Wall: Tile()
}
private data class Robot15(var pos: IntPoint, private val moves: List<Direction>) {
    fun move(warehouse: Warehouse) {
        for (dir in moves) {
            if (warehouse.move(pos, dir))
                pos = pos.move(1, dir)
        }
    }
}

private fun String.expand(): String {
    return buildString {
        for (c in this@expand)
            when(c) {
                '#' -> append("##")
                '.' -> append("..")
                '@' -> append("@.")
                'O' -> append("[]")
                else -> append(c)
            }
    }
}
private fun Warehouse.move(r: IntPoint, dir: Direction): Boolean {
    val np = r.move(1, dir)
    val stack = mutableListOf(np)
    val movable = mutableListOf<Pair<IntPoint, Tile>>()

    while(stack.isNotEmpty()) {
        val p = stack.removeFirst()
        if (this[p] is Tile.Free) continue
        if (this[p] is Tile.Wall) return false
        if (this[p] is Tile.Box) {
            stack.add(p.move(1, dir))
            movable.add(p to this[p])
            continue
        }

        if (dir.isHorizontal) {
            val rb = p.move(1, if (this[p] is Tile.LBox) Direction.EAST else Direction.WEST)
            movable.add(p to this[p])
            movable.add(rb to this[rb])
            stack.add(rb.move(1, dir))
        }

        if (dir.isVertical) {
            val rb = p.move(1, if (this[p] is Tile.LBox) Direction.EAST else Direction.WEST)
            movable.add(p to this[p])
            movable.add(rb to this[rb])
            stack.add(p.move(1, dir))
            stack.add(rb.move(1, dir))
        }
    }

    this[np] = Tile.Free
    for ((p, t) in movable.reversed()) {
        this[p] = Tile.Free
        this[p.move(1, dir)] = t
    }

    return true
}

fun main() {
    Logger.enabled = true
    fun parse(input: String, expand: Boolean): Pair<Robot15, Warehouse> {
        val (map, dirs) = input.split("\n\n")

        var start = Point(0, 0)
        val warehouse = (if (expand) map.expand() else map).lines().stringsToMatrix { p, c ->
            when (c) {
                'O' -> Tile.Box
                '[' -> Tile.LBox
                ']' -> Tile.RBox
                '#' -> Tile.Wall
                '@' -> { start = p; Tile.Free }
                else -> Tile.Free
            }
        }

        return Robot15(start, dirs.removeAll("\n").map { it.asCardinalDirection }) to warehouse
    }

    fun compute(input: String, expand: Boolean = false): Int {
        val (robot, warehouse) = parse(input, expand)

        robot.move(warehouse)
        return warehouse
            .filter { (_, t) -> t is Tile.Box || t is Tile.LBox }
            .sumOf { (p, _) -> p.x * 100 + p.y }
    }

    fun part1(input: String): Int = compute(input)
    fun part2(input: String): Int = compute(input, true)

    val input = readText("Day15")
    for ((fileId, expected) in partOneExpectedMap) {
        val actual = part1(readText("Day15-Test-$fileId"))
        check(expected == actual) { "--- PART 1 => Failed for file id: $fileId... expected: $expected, actual: $actual"}
    }
    println(part1(input))

    for ((fileId, expected) in partTwoExpectedMap) {
        val actual = part2(readText("Day15-Test-$fileId"))
        check(expected == actual) { "--- PART 2 => Failed for file id: $fileId... expected: $expected, actual: $actual"}
    }
     println(part2(input))
}
