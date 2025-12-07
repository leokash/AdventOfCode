
import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.Logger
import com.github.leokash.adventofcode.utils.collections.toCharMatrix
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.move
import com.github.leokash.adventofcode.utils.matrix.CharMatrix
import com.github.leokash.adventofcode.utils.matrix.getOrNull
import com.github.leokash.adventofcode.utils.matrix.indexOfFirst
import com.github.leokash.adventofcode.utils.readLines

private const val PART_ONE_EXPECTED = 21
private const val PART_TWO_EXPECTED = 40L

fun main() {
    Logger.enabled = false
    fun part1(input: List<String>): Int {
        return input.toCharMatrix().run {
            val beams = mutableSetOf(indexOfFirst { it == 'S' })
            val stack = mutableListOf(beams.first().move(1, Direction.SOUTH))

            while (stack.isNotEmpty()) {
                val next = stack.removeLast()
                if (next in beams) continue else when(getOrNull(next)) {
                    '.' -> next.apply { beams += this; stack.add(move(1, Direction.SOUTH)) }
                    '^' -> stack.addAll(listOf(next.move(1, Direction.WEST), next.move(1, Direction.EAST)))
                }
            }

            count { (p, c) -> c == '^' && p.move(1, Direction.NORTH) in beams }
        }
    }
    fun part2(input: List<String>): Long {
        fun getTimelines(pos: IntPoint, mat: CharMatrix, cache: MutableMap<IntPoint, Long>): Long {
            return cache.getOrPut(pos) {
                when (mat.getOrNull(pos)) {
                    '.', 'S' -> getTimelines(pos.move(1, Direction.SOUTH), mat, cache)
                    '^' -> getTimelines(pos.move(1, Direction.WEST), mat, cache) + getTimelines(pos.move(1, Direction.EAST), mat, cache)
                    else -> 1
                }
            }
        }

        return input.toCharMatrix().run { getTimelines(indexOfFirst { it == 'S' }, this, mutableMapOf()) }
    }

    val input = readLines("Day07")
    val inputTest = readLines("Day07-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
