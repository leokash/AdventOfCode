
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 3
private const val PART_TWO_EXPECTED = 6

fun main() {
    Logger.enabled = true
    fun compute(input: List<String>, partOne: Boolean = true): Int {
        val cc = CircularCounter(max = 99, start = 50)
        return input.fold(0) { count, string ->
            var clicks = count
            val op = string.first()
            val num = string.drop(1).toInt()
            (0..< num).forEach { _ ->
                val cur = if (op == 'L') cc.decrementAndGet() else cc.incrementAndGet()
                if (!partOne && cur == 0) clicks += 1
            }

            if (partOne && cc.get() == 0) clicks += 1
            clicks
        }
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day01")
    val inputTest = readLines("Day01-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
