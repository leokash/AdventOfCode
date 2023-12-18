
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.get
import com.github.leokash.adventofcode.utils.graphs.PathFinding
import com.github.leokash.adventofcode.utils.math.geometry.Point
import com.github.leokash.adventofcode.utils.math.geometry.next
import com.github.leokash.adventofcode.utils.matrix.IntMatrix
import com.github.leokash.adventofcode.utils.matrix.contains

private const val PART_ONE_EXPECTED = 102
private const val PART_TWO_EXPECTED_A = 94
private const val PART_TWO_EXPECTED_B = 71

private val sample_part_two_b = """
    111111111111
    999999999991
    999999999991
    999999999991
    999999999991
""".trimIndent()

fun main () {
    Logger.debug = true
    fun compute(input: List<String>, partOne: Boolean = true): Int {
        val end = Point(input.lastIndex, input[0].lastIndex)
        val city = IntMatrix(input.size, input[0].length) { x, y -> input[x,y].digitToInt() }

        data class State(val p: Point<Int>, val dir: Direction, val moves: Int = 0)

        val neighbors: (State) -> List<State> = { state ->
            listOf(state.dir, state.dir.rotate(90), state.dir.rotate(-90))
                .filter { dir ->
                    when (partOne) {
                        true -> if (state.moves < 3) true else dir != state.dir
                        false -> when (state.moves) {
                            in 0..3 -> dir == state.dir
                            in 4..9 -> true
                            else -> dir != state.dir
                        }
                    }
                }
                .mapNotNull { dir ->
                    val np = state.p.next(dir) ?: return@mapNotNull null
                    if (np in city) State(np, dir, if (dir == state.dir) state.moves + 1 else 1) else null
                }
        }

        return listOf(Direction.EAST, Direction.SOUTH).minOf {
            PathFinding.findShortestPath(State(Point(0, 0), it), PathFinding.Mode.GREEDY ({ _, rhs -> city[rhs.p] }, neighbors)) { s ->
                s.p == end && if (partOne) true else s.moves >= 4
            }?.getScore() ?: Int.MAX_VALUE
        }
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day17")
    val inputTest = readLines("Day17-Test")
    val inputTestB = sample_part_two_b.split("\n")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED_A)
    check(part2(inputTestB) == PART_TWO_EXPECTED_B)

    println(part1(input))
    println(part2(input))
}
