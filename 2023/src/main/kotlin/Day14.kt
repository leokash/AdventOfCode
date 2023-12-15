
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.get
import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.next
import com.github.leokash.adventofcode.utils.matrix.*

private val stopper = setOf('#', 'O')
private const val PART_ONE_EXPECTED = 136
private const val PART_TWO_EXPECTED = 64

fun main() {
    fun compute(input: List<String>, partOne: Boolean): Int {
        fun CharMatrix.calcLoad(): Int {
            return foldIndexed(0) { x, _, acc, c -> acc + if (c == 'O') rows - x else 0 }
        }

        fun CharMatrix.simulateTilt(dir: Direction) {
            fun move(pos: Point<Int>) {
                var cur = pos
                while (true) {
                    val next = cur.next(dir)
                    if (next == null || next !in this || this[next] in stopper)
                        return

                    this[cur] = '.'
                    this[next.also { cur = it }] = 'O'
                }
            }

            when (dir) {
                Direction.NORTH -> rowIndices.forEach { x -> row(x).forEachIndexed { y, c -> if (c == 'O') move(Point(x, y)) } }
                Direction.WEST  -> columnIndices.forEach { y -> column(y).forEachIndexed { x, c -> if (c == 'O') move(Point(x, y)) } }
                Direction.SOUTH -> rowIndices.reversed().forEach { x -> row(x).forEachIndexed { y, c -> if (c == 'O') move(Point(x, y)) } }
                Direction.EAST  -> columnIndices.reversed().forEach { y -> column(y).forEachIndexed { x, c -> if (c == 'O') move(Point(x, y)) } }

                else -> error("only moving in the cardinal directions")
            }
        }

        val mat = CharMatrix(input.size, input[0].length) { x, y ->
            input[x, y]
        }

        if (partOne) {
            return mat.apply { simulateTilt(Direction.NORTH) }.calcLoad()
        }

        var count = 0
        val amount = 1_000_000_000
        val visited = mutableMapOf<Int, Int>()
        val directions = listOf(Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST)

        while (count < 1_000_000_000) {
            count++
            directions.onEach {
                mat.simulateTilt(it)
            }

            val snapshot = mat.hashCode()
            val iteration = visited[snapshot]

            if (iteration != null) {
                val diff = count - iteration
                val reps = (amount - count) / diff

                count += reps * diff
            }

            visited[snapshot] = count
        }

        return mat.calcLoad()
    }

    fun part1(input: List<String>): Int = compute(input, true)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day14")
    val inputTest = readLines("Day14-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
