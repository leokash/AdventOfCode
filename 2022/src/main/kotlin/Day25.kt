
import com.github.leokash.adventofcode.utils.Logger
import com.github.leokash.adventofcode.utils.readLines

private const val PART_ONE_EXPECTED = 4890L
private const val PART_ONE_REVERSED_EXPECTED = "2=-1=0"
private val sampleTestCase = buildMap<String, Long> {
    put("1", 1)
    put("2", 2)
    put("1=", 3)
    put("1-", 4)
    put("10", 5)
    put("11", 6)
    put("12", 7)
    put("2=", 8)
    put("2-", 9)
    put("20", 10)
    put("1=0", 15)
    put("1-0", 20)
    put("1=11-2", 2022)
    put("1-0---0", 12345)
    put("1121-1110-1=0", 314159265)
}

fun main() {
    Logger.enabled = false
    val input = readLines("Day25")
    val sample = readLines("Day25-Test")

    sampleTestCase.onEach { (data, expected) ->
        val value = data.snafu
        check(value == expected) { "Snafu conversion failed! expected: $expected, actual: $value" }

        val reversed = value.snafu
        check(reversed == data) { "Reversed snafu conversion failed! expected: $data, actual: $reversed" }
    }

    check(sample.sumOf { it.snafu } == PART_ONE_EXPECTED)
    check(sample.sumOf { it.snafu }.snafu == PART_ONE_REVERSED_EXPECTED)
    println(input.sumOf { it.snafu }.snafu)
}

private val Char.snafu: Long get() {
    return when (this) {
        '-' -> -1L
        '=' -> -2L
        else -> digitToInt().toLong()
    }
}
private val Long.snafu: String get() {
    val base = "012=-"
    return generateSequence(this) { (it + 2) / 5 }
        .takeWhile { it != 0L }
        .map { base[(it % 5).toInt()] }
        .joinToString("")
        .reversed()
}
private val String.snafu: Long get() {
    fun pow(exp: Int): Long {
        fun raise(i: Int): Long = when(i) {
            1 -> 5L
            else -> 5L * raise(i - 1)
        }

        return if (exp == 0) 1L else raise(exp)
    }
    return this.reversed().foldIndexed(0) { i, acc, c -> acc + c.snafu * pow(i) }
}
