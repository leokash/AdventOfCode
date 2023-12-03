
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.*
import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.isNeighbor
import com.github.leokash.adventofcode.utils.math.geometry.neighbors

private typealias EngineNumber = Pair<Long, List<Point<Int>>>

private const val PART_ONE_EXPECTED = 4361L
private const val PART_TWO_EXPECTED = 467835L

private val excludeSet = (('0'..'9') + '.').toSet()

private fun String.nextEngineNumber(x: Int, y: Int): Pair<Int?, EngineNumber?> {
    var idx = y
    val buf = StringBuffer()
    val path = mutableListOf<Point<Int>>()
    while (idx <= lastIndex) {
        val char = this[idx]
        if (!char.isDigit()) break
        buf.append(char)
        path.add(Point(x, idx++))
    }

    if (buf.isEmpty()) return idx + 1 to null
    return idx to EngineNumber(buf.toString().toLong(), path)
}

fun main() {
    fun compute(input: List<String>, partOne: Boolean): Long {
        var x = 0
        var y = 0
        val engineNumbers = mutableListOf<EngineNumber>()
        while (true) {
            if (x !in input.indices) break
            if (y !in input[x].indices) { x++; y = 0; continue }
            val (ny, num) = input[x].nextEngineNumber(x, y)

            if (num != null) engineNumbers.add(num)
            y = ny ?: -1
        }

        if (partOne) {
            return engineNumbers
                .filter { (_, path) ->
                    path
                        .flatMap { p -> p.neighbors(true) { (_, p) -> p !in path && p in input } }
                        .any { (_, p) -> input[p] !in excludeSet }
                }
                .sumOf { (num, _) -> num }
        }

        return input.indicesOf('*').sumOf { p ->
            engineNumbers
                .filter { (_, path) -> path.any { it.isNeighbor(p, true) } }
                .let { list -> if (list.size > 1) list.product(Context<Long>()) { (num, _) -> num } else 0 }
        }
    }

    fun part1(input: List<String>): Long = compute(input, true)
    fun part2(input: List<String>): Long = compute(input, false)

    val input = readLines("Day03")
    val inputTest = readLines("Day03-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
