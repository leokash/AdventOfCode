
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.chunkedBy
import com.github.leokash.adventofcode.utils.math.geometry.LongPoint

private const val PART_ONE_EXPECTED = 480L

private val buttonRegex = """Button ([A,B]): X\+(\d+), Y\+(\d+)""".toRegex()

private data class ClawMachine(val buttons: List<Button>, private val target: LongPoint) {
    fun cost(): Long {
        val b0 = buttons[0]
        val b1 = buttons[1]
        val delta0 = target.y * b0.offset.x - target.x * b0.offset.y
        val delta1 = b0.offset.x * b1.offset.y - b0.offset.y * b1.offset.x

        val move1 = if (delta0 % delta1 == 0L) delta0 / delta1 else return 0
        return with(target.x - move1 * b1.offset.x) {
            val move0 = if (this % b0.offset.x == 0L) this / b0.offset.x else return 0
            move0 * b0.price + move1 * b1.price
        }
    }

    companion object {
        fun make(data: List<String>, prizeCorrection: Long): ClawMachine {
            return ClawMachine(
                data
                    .take(2)
                    .map { it.matchingGroups(buttonRegex) }
                    .map { Button(if (it[0] == "A") 3 else 1, LongPoint(it[1].toLong(), it[2].toLong())) } ,
                data[2].findAll(numberRegex).let { LongPoint(it[0].toLong() + prizeCorrection, it[1].toLong() + prizeCorrection) }
            )
        }
    }
}
private data class Button(val price: Int, val offset: LongPoint)

fun main() {
    fun compute(input: List<String>, correctPrize: Boolean = false): Long {
        return input
            .chunkedBy { it.isBlank() }
            .map { ClawMachine.make(it, if (correctPrize) 10_000_000_000_000L else 0L) }
            .sumOf { it.cost() }
    }

    fun part1(input: List<String>): Long = compute(input)
    fun part2(input: List<String>): Long = compute(input, true)

    val input = readLines("Day13")
    val inputTest = readLines("Day13-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    println(part2(input))
}
