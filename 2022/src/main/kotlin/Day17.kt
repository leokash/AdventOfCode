
@file:Suppress("all")

import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.CircularList
import com.github.leokash.adventofcode.utils.math.geometry.*
import com.github.leokash.adventofcode.utils.matrix.CharMatrix
import com.github.leokash.adventofcode.utils.matrix.lastRowIndex
import kotlin.math.min

private const val FLOOR = 5000
private const val PART_ONE_EXPECTED = 3068
private const val PART_TWO_EXPECTED = 1_514_285_714_288L

private val List<Point<Int>>.minX: Int get() = minOf { it.x } - 1
private fun CircularList<List<Point<Int>>>.next(xOffset: Int, yOffset: Int = 2): List<Point<Int>> {
    return next().map { Point(it.x + (xOffset - 3), it.y + yOffset) }
}

fun main() {
    Logger.debug = false
    val bounds = 0..6L
    fun rocks(): List<List<Point<Int>>> {
        return buildList {
            add(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3)))
            add(listOf(Point(-1, 0), Point(-1, y = 1), Point(-1, y = 2), Point(0, 1), Point(-2, 1)))
            add(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(-1, 2), Point(-2, 2)))
            add(listOf(Point(0, 0), Point(-1, 0), Point(-2, 0), Point(-3, 0)))
            add(listOf(Point(0, 0), Point(0, 1), Point(-1, 0), Point(-1, 1)))
        }
    }
    fun simulate(rock: List<Point<Int>>, winds: CircularList<Int>, predicate: (Point<Int>) -> Boolean) {
        fun dropWillCauseCollision(): Boolean {
            return rock.any { val p = Point(it.x + 1, it.y); p.x > FLOOR || predicate(p) }
        }
        fun windWillCauseCollision(wind: Int): Boolean {
            return rock.any { val p = Point(it.x, it.y + wind); p.y !in bounds || predicate(p) }
        }

        while (true) {
            val wind = winds.next()
            if (!windWillCauseCollision(wind))
                rock.onEach { it.y += wind }
            if (dropWillCauseCollision())
                break

            rock.onEach { r -> r.x += 1 }
        }
    }

    fun part1(input: String): Int {
        var xOff = FLOOR
        val cave = CharMatrix(FLOOR + 1, 7)
        val rocks = CircularList(list = rocks())
        val winds = CircularList(list = input.map { if (it == '<') -1 else 1 })
        repeat(2022) {
            val rock = rocks.next(xOff)
            simulate(rock, winds) { cave[it] == '#' }

            xOff = min(xOff, rock.minX)
            rock.onEach { cave[it] = '#' }
        }

        return FLOOR - xOff
    }
    fun part2(input: String): Long {
        data class State(val ri: Int, val wi: Int, val data: String)
        fun snapshot(ri: Int, wi: Int, height: Int, cave: CharMatrix): State {
            val rows = height..(min(cave.lastRowIndex, height + 20))
            return State(ri, wi, buildString { rows.onEach { append(cave.row(it).joinToString(separator = "")) } })
        }

        val cave = CharMatrix(FLOOR + 1, 7, '.')
        val rocks = CircularList(list = rocks())
        val winds = CircularList(list = input.map { if (it == '<') -1 else 1 })
        val amount = 1_000_000_000_000
        val states = mutableMapOf<State, Pair<Long, Int>>()

        var count = 0L
        var height = FLOOR
        var computedHeight = 0L

        while (count < amount) {
            val rock = rocks.next(height)
            simulate(rock, winds) { cave[it] == '#' }

            count++
            height = min(height, rock.minX)
            rock.onEach { cave[it] = '#' }

            val snapshot = snapshot(rocks.index, winds.index, height, cave)
            if (snapshot in states) {
                val (i, h) = states.getValue(snapshot)    // snapshot's index and height
                val nh = (FLOOR - height) - h             // computed height, current height minus snapshot's height
                val di = count - i                        // offset of added rocks:  count - snapshot's iteration
                val reps = (amount - count).floorDiv(di)  // amount of times we need to repeat cycle

                count += reps * di
                computedHeight += reps * nh
            }

            states[snapshot] = (count to (FLOOR - height))
        }

        return (FLOOR - height) + computedHeight
    }

    val input = readText("Day17")
    val sample = readText("Day17-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}
