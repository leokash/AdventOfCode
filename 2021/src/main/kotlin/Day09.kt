
import com.github.leokash.adventofcode.utils.geometry.points.ints.Point
import com.github.leokash.adventofcode.utils.readLines
import com.github.leokash.adventofcode.utils.matrix.IntMatrix
import com.github.leokash.adventofcode.utils.matrix.neighbors
import com.github.leokash.adventofcode.utils.matrix.foldIndexed

private fun IntMatrix.basin(x: Int, y: Int, visited: MutableList<Point>): Int {
    visited.add(Point(x, y))
    val predicate = { i: Int, j: Int, num: Int ->
        num < 9 && visited.find { (px, py) -> px == i && py == j } == null
    }
    return neighbors(x, y, predicate = predicate)
        .onEach { (point, _) -> visited.add(point) }
        .fold(0) { sum, (point, _) -> sum + 1 + basin(point.x, point.y, visited) }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = IntMatrix(input.size, input[0].trim().length).also { mat ->
            for ((x, string) in input.withIndex()) {
                string.trim().toList().forEachIndexed { y, c ->
                    mat[x, y] = c.digitToInt()
                }
            }
        }
        return grid.foldIndexed(0) { x, y, sum, num ->
            sum + grid.neighbors(x, y).let { neighbors ->
                if (neighbors.all { (_, n) -> n > num }) num + 1 else 0
            }
        }
    }
    fun part2(input: List<String>): Int {
        val grid = IntMatrix(input.size, input[0].trim().length).also { mat ->
            for ((x, string) in input.withIndex()) {
                string.trim().toList().forEachIndexed { y, c ->
                    mat[x, y] = c.digitToInt()
                }
            }
        }

        return grid.foldIndexed(mutableListOf<Int>()) { x, y, arr, num ->
            arr.also { it.add(grid.neighbors(x, y).let { neighbors ->
                if (neighbors.all { (_, n) -> n > num }) grid.basin(x, y, mutableListOf()) + 1 else 0
            }) }
        }.sorted().takeLast(3).reduce { acc, i -> acc * i }
    }

    val input = readLines("Day09")
    val inputTest = readLines("Day09-Test")

    check(part1(inputTest) == 15)
    check(part2(inputTest) == 1134)

    println(part1(input))
    println(part2(input))
}
