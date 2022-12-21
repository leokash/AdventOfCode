
import com.github.leokash.adventofcode.utils.readLines
import com.github.leokash.adventofcode.utils.matrix.fold
import com.github.leokash.adventofcode.utils.matrix.IntMatrix
import com.github.leokash.adventofcode.utils.geometry.points.ints.Point
import kotlin.math.*

private fun IntMatrix.add(vector: Vec4, diagonal: ((Vec4) -> List<Point>)? = null) {
    val (x1,y1,x2,y2) = vector
    when {
        x1 == x2 -> (min(y1,y2)..(max(y1,y2))).forEach { this[it, x1]++ }
        y1 == y2 -> (min(x1,x2)..(max(x1,x2))).forEach { this[y1, it]++ }
        diagonal != null -> diagonal(vector).forEach { (x, y) -> this[y, x]++ }
    }
}

private data class Vec4(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

fun main() {
    fun size(input: List<Vec4>): Point {
        return input.fold(Point()) { (px, py), (x1,y1,x2,y2) ->
            Point(max(max(x1, x2), px), max(max(y1, y2), py))
        }.let { (x,y) -> Point(x+1, y+1) }
    }
    fun parse(input: List<String>): List<Vec4> {
        return input.map { s ->
            s.split("->").let {
                it.map { s ->
                    s.split(',')
                }
            }
        }.map { arr ->
            Vec4(
                arr[0][0].trim().toInt(),
                arr[0][1].trim().toInt(),
                arr[1][0].trim().toInt(),
                arr[1][1].trim().toInt())
        }
    }

    val diagonal: (Vec4) -> List<Point> = { vec ->
        val (x1, y1, x2, y2) = vec
        (0..abs(x1 - x2)).fold(listOf()) { arr, i ->
            arr + Point(x1 + if (x1 < x2) i else -i, y1 + if (y1 < y2) i else -i)
        }
    }

    fun part1(input: List<Vec4>): Int {
        val (rows, columns) = size(input)
        return IntMatrix(rows, columns).also {
            for (vec in input) { it.add(vec) }
        }.fold(0) { acc, n -> acc + if (n >= 2) 1 else 0 }
    }
    fun part2(input: List<Vec4>): Int {
        val (rows, columns) = size(input)
        return IntMatrix(rows, columns).also {
            for (vec in input) { it.add(vec, diagonal) }
        }.fold(0) { acc, n -> acc + if (n >= 2) 1 else 0 }
    }

    val input = parse(readLines("Day05"))
    val inputTest = parse(readLines("Day05-Test"))

    check(part1(inputTest) == 5)
    check(part2(inputTest) == 12)

    println(part1(input))
    println(part2(input))
}
