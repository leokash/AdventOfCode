
import com.github.leokash.adventofcode.utils.*
import com.github.shiguruikai.combinatoricskt.permutationsWithRepetition

private const val PART_ONE_EXPECTED = 3749L
private const val PART_TWO_EXPECTED = 11387L

data object Add: Op {
    override fun invoke(lhs: Long, rhs: Long): Long = lhs + rhs
}
data object Mul: Op {
    override fun invoke(lhs: Long, rhs: Long): Long = lhs * rhs
}
data object Con: Op {
    override fun invoke(lhs: Long, rhs: Long): Long = "$lhs$rhs".toLong()
}
private sealed interface Op {
    operator fun invoke(lhs: Long, rhs: Long): Long
}

private val numRgx = """\d+""".toRegex()
private val operations = listOf(Add, Mul)

private fun List<Long>.findRightCalibration(answer: Long, concat: Boolean): Boolean {
    return operations
        .let { if (concat) it + Con else it }
        .permutationsWithRepetition(lastIndex)
        .any { reduceIndexed { i, lhs, rhs ->  it[i - 1](lhs, rhs) } == answer }
}

fun main() {
    fun compute(input: List<String>, concat: Boolean = false): Long {
        return input
            .map { it.findAll(numRgx).map(String::toLong) }
            .sumOf { arr -> if(arr.drop(1).findRightCalibration(arr[0], concat)) arr[0] else 0 }
    }

    fun part1(input: List<String>): Long = compute(input)
    fun part2(input: List<String>): Long = compute(input, true)

    val input = readLines("Day07")
    val inputTest = readLines("Day07-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
