
import collections.chunkedByIndexed
import matrix.*

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
private fun <T> Matrix<T>.chunked(x: Int, y: Int): Pair<List<List<T>>, List<List<T>>> {
    return row(x).chunkedByIndexed { i, _ -> i == y } to column(y).chunkedByIndexed { i, _ -> i == x }
}

private fun Tree.isVisible(x: Int, y: Int, mat: Matrix<Tree>): Boolean {
    return mat.chunked(x, y).let { (rows, columns) ->
        rows.any { it.all { num -> num < this } } || columns.any { it.all { num -> num < this } }
    }
}
private fun Tree.calcScenicScore(x: Int, y: Int, mat: Matrix<Tree>): Int {
    tailrec fun score(count: Int = 0, point: Point?, dir: Direction): Int {
        val tmp = point ?: return count
        val tree = mat.getOrNull(tmp) ?: return count
        return if (tree >= this) count + 1 else score(count + 1, tmp.next(dir), dir)
    }

    val p = Point(x, y)
    return score(point = p.next(Direction.East), dir = Direction.East) *
           score(point = p.next(Direction.West), dir = Direction.West) *
           score(point = p.next(Direction.North), dir = Direction.North) *
           score(point = p.next(Direction.South), dir = Direction.South)
}
