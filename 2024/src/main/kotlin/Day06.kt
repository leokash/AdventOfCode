
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.toCharMatrix
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.next
import com.github.leokash.adventofcode.utils.matrix.*

private const val PART_ONE_EXPECTED = 41
private const val PART_TWO_EXPECTED = 6

private typealias State = Pair<IntPoint, Direction>

private fun CharMatrix.simulate(start: IntPoint, obstacle: IntPoint?): Pair<Boolean, Set<IntPoint>> {
    var loop = false
    val set = mutableSetOf<State>()
    val path = generateSequence(start to Direction.NORTH) {
        if (it in set)
            return@generateSequence null.also { loop = true }

        set += it
        val (point, dir) = it
        point.next(dir).let { tmp ->
            tmp?.let { next ->
                when (if (next == obstacle) 'O' else getOrNull(next)) {
                    '.' -> next to dir
                    '#', 'O' -> point to dir.rotate(90)
                    else -> null
                }
            }
        }
    }
        .map { (p, _) -> p }
        .toSet()

    return loop to path
}

private fun List<String>.parse(): Pair<IntPoint, CharMatrix> {
    var start = Point(0, 0)
    val matrix = toCharMatrix { x, y, c -> if (c == '^') '.'.also { start = Point(x, y) } else c }
    return start to matrix
}

fun main() {
    Logger.debug = true
    fun part1(input: List<String>): Int = input.parse().let { (s, mat) -> mat.simulate(s, null).second.size }
    fun part2(input: List<String>): Int = input.parse().let { (s, mat) ->  mat.simulate(s, null).second.drop(1).count {  mat.simulate(s, it).first } }

    val input = readLines("Day06")
    val inputTest = readLines("Day06-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
