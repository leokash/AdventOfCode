
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.toCharMatrix
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.next
import com.github.leokash.adventofcode.utils.matrix.*

private const val PART_ONE_EXPECTED = 41
private const val PART_TWO_EXPECTED = 6

private typealias State = Pair<IntPoint, Direction>

private fun CharMatrix.simulate(start: IntPoint, obstacle: IntPoint? = null): Pair<Boolean, Set<IntPoint>> {
    var inLoop = false
    val visited = mutableSetOf<State>()
    val guardPath = generateSequence(start to Direction.NORTH) { (point, dir) ->
        point.next(dir).let { tmp ->
            tmp?.let { next ->
                when (if (next == obstacle) '#' else getOrNull(next)) {
                    '.' -> next to dir
                    '#' -> point to dir.rotate(90)
                    else -> null
                }
            }
        }
    }
        .onEach { inLoop = it in visited }
        .takeWhile { !inLoop }
        .onEach { visited.add(it) }
        .map { it.first }
        .toSet()

    return inLoop to guardPath
}

private fun List<String>.parse(): Pair<IntPoint, CharMatrix> {
    return toCharMatrix().let { it.indexOfFirst { c -> c == '^' }.also { idx -> it[idx] = '.' } to it }
}

fun main() {
    Logger.debug = true
    fun part1(input: List<String>): Int = input.parse().let { (s, mat) -> mat.simulate(s).second.size }
    fun part2(input: List<String>): Int = input.parse().let { (s, mat) -> mat.simulate(s).second.drop(1).count { mat.simulate(s, it).first } }

    val input = readLines("Day06")
    val inputTest = readLines("Day06-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
