
import matrix.*

private fun simulate1(mat: IntMatrix, steps: Int = 100): Int {
    return (1..steps).fold(0) { acc, _ ->
        mat.forEachIndexed { x, y, n ->
            mat[x, y] = n + 1
        }
        mat.foldIndexed(acc) { x, y, inAcc, _ -> inAcc + mat.computeFlashes(x, y) }
    }
}
private fun simulate2(mat: IntMatrix, steps: Int = 500): Int {
    repeat(steps) {
        mat.forEachIndexed { x, y, n ->
            mat[x, y] = n + 1
        }
        mat.forEachIndexed { x, y, _ -> mat.computeFlashes(x, y) }
        if (mat.fold(0) { acc, num -> acc + if (num == 0) 1 else 0 } == mat.size())
            return it + 1
    }

    return -1
}

private val validRange = 1..9
private fun IntMatrix.computeFlashes(x: Int, y: Int): Int {
    return if (this[x, y] < 10) 0 else {
        this[x, y] = 0
        1 + neighbors(x, y, true) { _, _, num -> num in validRange }
            .onEach { (p, num) -> this[p.x, p.y] = num + 1 }
            .fold(0) { acc, (p, _) -> acc + this.computeFlashes(p.x, p.y) }
    }
}

fun main() {
    fun parse(input: List<String>): IntMatrix {
        return IntMatrix(10, 10).also {
            for ((x, string) in input.withIndex()) {
                string.trim().toList().forEachIndexed { y, c ->
                    it[x, y] = c.digitToInt()
                }
            }
        }
    }
    fun part1(input: List<String>, steps: Int): Int {
        val mat = parse(input)
        return simulate1(mat, steps)
    }
    fun part2(input: List<String>, steps: Int): Int {
        val mat = parse(input)
        return simulate2(mat, steps)
    }

    val input = readLines("Day11")
    val inputTest = readLines("Day11-Test")

    check(part1(inputTest, 100) == 1656)
    check(part2(inputTest, 250) == 195)

    println(part1(input, 100))
    println(part2(input, 250))
}
