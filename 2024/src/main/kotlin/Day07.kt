
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 3749L
private const val PART_TWO_EXPECTED = 11387L

private fun interface Op: (Long, Long) -> Long

private val add = Op { lhs: Long, rhs: Long -> lhs + rhs }
private val mul = Op { lhs: Long, rhs: Long -> lhs * rhs }
private val concat = Op { lhs: Long, rhs: Long -> "$lhs$rhs".toLong() }

private fun List<Long>.validate(answer: Long, operations: List<Op>): Boolean {
    fun validate(value: Long, values: List<Long>): Boolean {
        if (value > answer) return false
        if (values.isEmpty()) return value == answer
        return operations.any { validate(it(value, values[0]), values.subList(1, values.size)) }
    }

    return validate(this[0], subList(1, size))
}

fun main() {
    fun compute(input: List<String>, operations: List<Op>): Long {
        return input
            .map { it.findAll(numberRegex).map(String::toLong) }
            .sumOf { arr -> if(arr.drop(1).validate(arr[0], operations)) arr[0] else 0 }
    }

    fun part1(input: List<String>): Long = compute(input, listOf(add, mul))
    fun part2(input: List<String>): Long = compute(input, listOf(add, mul, concat))

    val input = readLines("Day07")
    val inputTest = readLines("Day07-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
