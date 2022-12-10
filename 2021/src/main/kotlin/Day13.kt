
import matrix.count
import matrix.IntMatrix
import matrix.MatrixStringifier

private fun IntMatrix.print() {
    println(MatrixStringifier().stringify(this) { if (it > 0) "$SPRITE_ON" else "$SPRITE_OFF" })
}

private fun IntMatrix.fold(cmd: Fold): IntMatrix {
    val (axis, idx) = cmd
    return if (axis == "x") foldHorizontally(idx) else foldVertically(idx)
}
private fun IntMatrix.foldVertically(idx: Int): IntMatrix {
    val mat = IntMatrix(idx, columns) { x, y -> this[x, y] }
    for (x in (idx+1 until rows)) {
        for (y in (0 until columns)) {
            this[x, y].let { num ->
                if (num > 0) {
                    mat[2*idx - x, y] += num
                }
            }
        }
    }
    return mat
}
private fun IntMatrix.foldHorizontally(idx: Int): IntMatrix {
    val mat = IntMatrix(rows, idx) { x, y -> this[x, y] }
    for (x in (0 until rows)) {
        for (y in (idx+1 until columns)) {
            this[x, y].let { num ->
                if (num > 0) {
                    mat[x, 2*idx - y] += num
                }
            }
        }
    }
    return mat
}

private data class Fold(val axis: String, val index: Int)

fun main() {
    fun parse(input: List<String>): Pair<List<Fold>, IntMatrix> {
        val folds = input
            .takeLastWhile { it.startsWith("fold") }
            .map { it.replace("fold along ", "").trim().split("=") }
            .map { (lhs, rhs) -> Fold(lhs, rhs.toInt()) }
        val coordinates = input
            .asSequence()
            .filter { it.contains(",") }.map { it.trim().split(",") }
            .map { (y, x) -> Point(x.toInt(), y.toInt()) }

        val maxX = coordinates.maxOf { it.x }
        val maxY = coordinates.maxOf { it.y }
        return folds to IntMatrix(maxX + 1, maxY + 1) { _, _ -> 0 }.also { mat ->
            for ((x, y) in coordinates) {
                mat[x, y] += 1
            }
        }
    }
    fun part1(input: List<String>): Int {
        val (cmd, mat) = parse(input)
        return mat.fold(cmd.first()).count { it > 0 }
    }
    fun part2(input: List<String>): Int {
        val (commands, matrix) = parse(input)
        var folded = matrix
        for (cmd in commands)
            folded = folded.fold(cmd)
        folded.print()
        return 0
    }

    val input = readLines("Day13")
    val inputTest = readLines("Day13-Test")

    check(part1(inputTest) == 17)
    check(part2(inputTest) == 0)

    println(part1(input))
    println(part2(input))
}