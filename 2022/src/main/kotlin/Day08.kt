
import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.collections.product
import com.github.leokash.adventofcode.utils.geometry.points.ints.Point
import com.github.leokash.adventofcode.utils.readLines
import com.github.leokash.adventofcode.utils.matrix.*

private const val PART_ONE_EXPECTED = 21
private const val PART_TWO_EXPECTED = 8

private typealias Tree = Int

fun main() {
    fun part1(input: List<String>): Int {
        return with(parse(input)) {
            countBy { x, y, tree ->
                if (isEdge(x, y)) true else tree.isVisible(x, y, this)
            }
        }
    }
    fun part2(input: List<String>): Int {
        return with(parse(input)) {
            maxBy { x, y, tree ->
                if (isEdge(x, y)) 0 else tree.calcScenicScore(x, y, this)
            }
        }
    }

    val input = readLines("Day08")
    val inputTest = readLines("Day08-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}

private fun parse(input: List<String>): IntMatrix {
    return IntMatrix(input.size, input[0].length) { i, j ->
        input[i][j].code - '0'.code
    }
}

private fun Matrix<*>.isEdge(x: Int, y: Int): Boolean {
    return x == 0 || y == 0 || x == lastRowIndex || y == lastColumnIndex
}

private fun Tree.isVisible(x: Int, y: Int, mat: Matrix<Tree>): Boolean {
    tailrec fun isVisible(i: Int, j: Int, dir: Direction): Boolean {
        val next = Point(i, j).next(dir)
        val value = next?.let { mat.getOrNull(it)?.let { tree -> tree to it } } ?: return true
        return if (value.first >= this) false else isVisible(value.second.x, value.second.y, dir)
    }

    return Direction.cardinals.any { isVisible(x, y, it) }
}
private fun Tree.calcScenicScore(x: Int, y: Int, mat: Matrix<Tree>): Int {
    tailrec fun score(i: Int, j: Int, count: Int, dir: Direction): Int {
        val point = Point(i, j).next(dir)
        val tree = point?.let { mat.getOrNull(it) } ?: return count
        return if (tree >= this) count + 1 else score(point.x, point.y, count + 1, dir)
    }

    return Direction.cardinals.product { dir -> score(x, y, 0, dir) }
}
