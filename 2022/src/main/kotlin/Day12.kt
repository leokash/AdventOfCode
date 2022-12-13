
@file:Suppress("MagicNumber")

import matrix.IntMatrix
import matrix.neighbors
import java.util.*
import PathFinding.Mode.BFS as BFS

private const val PART_ONE_EXPECTED = 31
private const val PART_TWO_EXPECTED = 29

private val Char.elevation: Int get() {
    return when(this) {
        'E' -> 27
        'S' -> 0
        in 'a'..'z' -> (this.code - 'a'.code) + 1

        else -> error("Invalid char provided: $this")
    }
}
private val Int.elevationToChar: Char get() {
    return when(this) {
        0  -> 'S'
        27 -> 'E'
        in 1..26 -> ((this - 1) + 'a'.code).toChar()

        else -> error("Invalid cost provided: $this")
    }
}
private fun List<String>.indexOf(char: Char): Point? {
    for (x in indices)
        for (y in this[x].indices)
            if (this[x][y] == char) return Point(x, y)
    return null
}
private fun List<String>.allIndicesOf(char: Char): List<Point> {
    return buildList {
        for (x in this@allIndicesOf.indices)
            for (y in this@allIndicesOf[x].indices)
                if (this@allIndicesOf[x][y] == char) add(Point(x, y))
    }
}

fun main() {
    fun compute(input: List<String>, start: Char, finish: Char, singleStart: Boolean): Int {
        fun log(path: List<Point>, mat: IntMatrix) {
            log {
                val size = path.size - 1
                "steps: $size, path: ${ path.joinToString { c -> "${ mat[c].elevationToChar }" } }"
            }
        }
        fun neighbors(from: Point, mat: IntMatrix): List<Point> {
            val lhs = mat[from]
            return mat
                .neighbors(from.x, from.y) { _, _, rhs -> rhs - lhs <= 1 }
                    .map { (p, _) -> p }
        }

        val end = input.indexOf(finish) ?: error("Finish '$finish' not found")
        val mat = IntMatrix(input.size, input[0].length) { x, y -> input[x][y].elevation }

        return (if (singleStart)
                    listOf(input.indexOf(start) ?: error("Start '$start' not found"))
                else
                    input.allIndicesOf(start)
                ).mapNotNull { s ->
            PathFinding
                .findShortestPath(s, end, BFS { p -> neighbors(p, mat) })
                ?.getPath()
                ?.also { log(it, mat) }
                ?.size
                ?.let { it - 1 }
        }.min()
    }

    Logger.debug = false
    fun part1(input: List<String>): Int = compute(input, 'S', 'E', true)
    fun part2(input: List<String>): Int = compute(input, 'a', 'E', false)

    val input = readLines("Day12")
    val sample = readLines("Day12-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}
