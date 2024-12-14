
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.toCharMatrix
import com.github.leokash.adventofcode.utils.math.geometry.*

private val partOneExpectedMap = mapOf("A" to 140, "B" to 772, "C" to 1930)
private val partTwoExpectedMap = mapOf("A" to 80, "B" to 436, "C" to 1206, "D" to 236, "E" to 368)

private typealias Plot = List<IntPoint>

private fun Plot.perimeter(): Int {
    return sumOf { it.neighbors { (_, p) -> p !in this }.size }
}

private fun Plot.sides(): Int {
    fun IntPoint.hasBorder(dir: Direction): Boolean {
        return this in this@sides && move(1, dir) !in this@sides
    }

    return sumOf { p ->
        Direction.cardinals.count { dir ->
            p.hasBorder(dir) && !p.move(1, dir.rotate(90)).hasBorder(dir)
        }
    }
}

private fun List<Pair<IntPoint, Char>>.getPlots(): List<Plot> {
    val stack = this.toMutableList()
    val result = mutableListOf(mutableListOf(stack.removeLast().first))

    while (stack.isNotEmpty()) {
        var added = false
        for0@for (i in stack.indices.reversed()) {
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
            result.add(mutableListOf(stack.removeLast().first))
        }
    }

    return result
}

fun main() {
    fun compute(input: List<String>, usingSides: Boolean = false): Int {
        val perimeter = if (usingSides) Plot::sides else Plot::perimeter

        return input
            .toCharMatrix()
            .groupBy { (_, c) -> c }
            .filter { it.key != ' ' }
            .mapValues { (_, arr) -> arr.getPlots() }
            .values
            .sumOf {
                it.sumOf { plot ->
                    plot.size * perimeter(plot)
                }
        }
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    for ((fileId, expected) in partOneExpectedMap) {
        val actual = part1(readLines("Day12-Test-$fileId"))
        check(expected == actual) { "--- PART 1 => Failed for file id: $fileId... expected: $expected, actual: $actual"}
    }


    for ((fileId, expected) in partTwoExpectedMap) {
        val actual = part2(readLines("Day12-Test-$fileId"))
        check(expected == actual) { "--- PART 2 => Failed for file id: $fileId... expected: $expected, actual: $actual"}
    }


    val input = readLines("Day12")

    println(part1(input))
    println(part2(input))
}
