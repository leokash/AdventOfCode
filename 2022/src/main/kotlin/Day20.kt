
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 3L
private const val PART_TWO_EXPECTED = 1623178306L

private data class Number(val index: Int, val value: Long)

fun main() {
    Logger.debug = true
    fun index(i: Int, num: Long, max: Int): Int {
        val idx = (i + num) % max
        return if (idx < 0) idx.toInt() + max else idx.toInt()
    }
    fun compute(input: List<String>, key: Long = 1, mix: Int = 1): Long {
        val size = input.size - 1
        val mapped = parse(input, key)
        val numbers = mapped.values.toMutableList()

        repeat(mix) {
            for (i in 0..size) {
                val num = mapped[i] ?: error("number should be present")
                val idx = index(numbers.indexOf(num), num.value, size)
                numbers.remove(num)
                when (idx) {
                    0 -> numbers.add(num)
                    size -> numbers.add(0, num)
                    else -> numbers.add(idx, num)
                }
            }
        }

        val zIdx = numbers.indexOfFirst { it.value == 0L }
        return listOf(1000L, 2000L, 3000L).sumOf { n -> numbers[(zIdx + n).mod(size + 1)].value }
    }

    fun part1(input: List<String>) = compute(input)
    fun part2(input: List<String>) = compute(input, 811589153, 10)

    val input = readLines("Day20")
    val sample = readLines("Day20-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}

private fun parse(input: List<String>, key: Long): Map<Int, Number> {
    return buildMap {
        for ((i, s) in input.withIndex())
            put(i, Number(i, s.toLong() * key))
    }
}
