
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.*
import com.github.leokash.adventofcode.utils.math.geometry.*
import com.github.leokash.adventofcode.utils.matrix.*

private const val PART_ONE_EXPECTED = 14
private const val PART_TWO_EXPECTED = 34

/*
   INITIAL         PART 1         PART 2
............   ......#....#   ##....#....#
........0...   ...#....0...   .#.#....0...
.....0......   ....#0....#.   ..#.#0....#.
.......0....   ..#....0....   ..##...0....
....0.......   ....0....#..   ....0....#..
......A.....   .#....A.....   .#...#A....#
............   ...#........   ...#..#.....
............   #......#....   #....#.#....
........A...   ........A...   ..#.....A...
.........A..   .........A..   ....#....A..
............   ..........#.   .#........#.
............   ..........#.   ...#......##
*/

private fun CharMatrix.findAntinodes(char: Char, lhs: IntPoint, resonate: Boolean): List<IntPoint> {
    return this
        .filter { (rhs, c) -> c == char && lhs != rhs && lhs.manhattanDistance(rhs) >= 2 }
        .flatMap { (rhs, _) ->
            val distLhs = lhs - rhs
            val distRhs = rhs - lhs
            var tmpForward: IntPoint? = (if (resonate) rhs else lhs) + distLhs
            var tmpBackward: IntPoint? = (if (resonate) rhs else lhs) + distRhs
            buildList {
                while (tmpForward != null || tmpBackward != null) {
                    tmpForward?.let { if (it in this@findAntinodes) add(it) }
                    tmpForward?.let { if (it in this@findAntinodes) add(it) }

                    if (!resonate) break
                    tmpForward = tmpForward?.let { if (it in this@findAntinodes) it + distLhs else null }
                    tmpBackward = tmpBackward?.let { if (it in this@findAntinodes) it + distRhs else null }
                }
            }
        }
}

fun main() {
    fun compute(input: List<String>, resonate: Boolean = false): Int {
        val mat = input.toCharMatrix()
        return mat
            .filter { _, c -> c != '.' }
            .flatMap { (p, c) -> mat.findAntinodes(c, p, resonate) }
            .distinct()
            .size
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    val input = readLines("Day08")
    val inputTest = readLines("Day08-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
