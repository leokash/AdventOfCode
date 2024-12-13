
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.chunkedBy
import com.github.leokash.adventofcode.utils.math.geometry.LongPoint

private const val PART_ONE_EXPECTED = 480L

private val buttonRegex = """Button ([A,B]): X\+(\d+), Y\+(\d+)""".toRegex()

private data class ClawMachine(val buttons: List<Button>, private val target: LongPoint) {
    fun cost(): Long {
        val delta0 = target.y * buttons[0].offset.x - target.x * buttons[0].offset.y
        val delta1 = buttons[0].offset.x * buttons[1].offset.y - buttons[0].offset.y * buttons[1].offset.x

        val button1Move = if (delta0 % delta1 == 0L) delta0 / delta1 else return 0
        return with(target.x - button1Move * buttons[1].offset.x) {
            val button0Move = if (this % buttons[0].offset.x == 0L) this / buttons[0].offset.x else return 0
            button0Move * 3 + button1Move
        }
    }

    companion object {
        fun make(data: List<String>, prizeCorrection: Long): ClawMachine {
            return ClawMachine(
                data
                    .take(2)
                    .map { it.matchingGroups(buttonRegex) }
                    .map { Button((if (it[0] == "A") 3 else 1).toInt(), LongPoint(it[1].toLong(), it[2].toLong())) } ,
                data[2].findAll(numberRegex).let { LongPoint(it[0].toInt() + prizeCorrection, it[1].toInt() + prizeCorrection) }
            )
        }
    }
}
private data class Button(val price: Int, val offset: LongPoint)

fun main() {
    Logger.debug = true
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
