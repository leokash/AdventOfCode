
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.geometry.*
import com.github.leokash.adventofcode.utils.matrix.CharMatrix
import com.github.leokash.adventofcode.utils.matrix.Matrix
import com.github.leokash.adventofcode.utils.matrix.MatrixStringifier
import com.github.leokash.adventofcode.utils.matrix.ofChars
import com.github.leokash.adventofcode.utils.matrix.contains

private val partOneExpectedMap = mapOf("A" to 140, "B" to 772, "C" to 1930)
private val partTwoExpectedMap = mapOf("A" to 80, "B" to 436, "C" to 1206, "D" to 236, "E" to 368)

private typealias Plot = List<IntPoint>

private fun Plot.perimeter(): Int {
    return sumOf { it.neighbors { (_, p) -> p !in this }.size }
}

private fun Plot.sides(mat: CharMatrix): Int {
    val id = mat[this[0]]
    return Direction.cardinals.sumOf { dir ->
        var count = 0
        val visited = mutableSetOf<IntPoint>()
        for (p in this) {
            if (p in visited) continue
            if (mat[p.move(1, dir)] != id) {
                count++
                visited += p
                listOf(90, -90).map { dir.rotate(it) }.forEach { sideDir ->
                    var cur = p
                    do {
                        cur = cur.move(1, sideDir).also { visited += it }
                    } while (cur in mat && mat[cur] == id && mat[cur.move(1, dir)] != id)
                }
            }
        }

        count
    }
}

private fun List<Pair<IntPoint, Char>>.getPlots(): List<Plot> {
    val stack = this.toMutableList()
    val result = mutableListOf(mutableListOf(stack.removeFirst().first))

    while (stack.isNotEmpty()) {
        var added = false
        for0@for (i in stack.indices) {
            for (plot in result) {
                val p = stack[i].first
                if (plot.any { it.isNeighbor(p) }) {
                    plot.add(p)
                    stack.removeAt(i)
                    added = true
                    break@for0
                }
            }
        }

        if (!added) {
            result.add(mutableListOf(stack.removeFirst().first))
        }
    }

    return result
}

fun main() {
    Logger.debug = true
    fun compute(input: List<String>, usingSides: Boolean = false): Int {
        val mat = Matrix.ofChars(input.size + 2, input[0].length + 2).apply {
            for ((x, row) in input.withIndex())
                for (y in row.indices)
                    this[Point(x + 1, y + 1)] = input[x][y]
        }

        return mat
            .groupBy { (_, c) -> c }
            .filter { it.key != ' ' }
            .mapValues { (_, arr) -> arr.getPlots() }
            .onEach { (c, v) -> val plants = v.flatMap { it }; log { MatrixStringifier.stringify(mat) { p, _ -> if (p in plants) "$c" else " " } } }
            .values
            .sumOf {
                it.sumOf { plot ->
                    plot.size * if (usingSides) plot.sides(mat) else plot.perimeter()
                }
        }
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    for ((fileId, expected) in partOneExpectedMap) {
        val actual = part1(readLines("Day12-Test-$fileId"))
        check(expected == actual) { "--- PART 1 => Failed with file id: $fileId... expected: $expected, actual: $actual"}
    }


    for ((fileId, expected) in partTwoExpectedMap) {
        val actual = part2(readLines("Day12-Test-$fileId"))
        check(expected == actual) { "--- PART 2 => Failed with file id: $fileId... expected: $expected, actual: $actual"}
    }


    val input = readLines("Day12")

    println(part1(input))
    println(part2(input))
}
