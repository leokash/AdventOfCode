
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.toIntMatrix
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.neighbors
import com.github.leokash.adventofcode.utils.matrix.IntMatrix
import com.github.leokash.adventofcode.utils.matrix.contains

private const val PART_ONE_EXPECTED = 36
private const val PART_TWO_EXPECTED = 81

fun main() {
    fun compute(input: List<String>, countDistinct: Boolean = false): Int {
        fun walkTrail(loc: Int, p: IntPoint, mat: IntMatrix, visited: MutableSet<IntPoint> = mutableSetOf()): Int {
            var score = 0
            visited.add(p)
            for ((_, np) in p.neighbors { (_, np) -> np !in visited && np in mat && (mat[np] - loc) == 1 }) {
                visited.add(np)
                score += if (mat[np] == 9) 1 else walkTrail(mat[np], np, mat, if (countDistinct) visited.map { it }.toMutableSet() else visited)
            }

            return score
        }

        val mat = input.toIntMatrix()
        return mat
            .filter { (_, n) -> n == 0 }
            .sumOf { (p, n) -> walkTrail(n, p, mat) }
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    val input = readLines("Day10")
    val inputTest = readLines("Day10-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
