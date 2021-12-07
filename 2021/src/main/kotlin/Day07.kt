
import kotlin.math.abs

private fun computeSteps(input: List<Int>, f: (Int, Int) -> Int): Int {
    return (0..input.maxOf { it })
        .fold(listOf<Int>()) { acc, i -> acc + input.sumOf { f(i, it) } }
        .minOf { it }
}

fun main() {
    fun part1(input: List<Int>): Int {
        return computeSteps(input) { lhs, rhs -> abs(lhs - rhs) }
    }
    fun part2(input: List<Int>): Int {
        fun Int.steps(): Int {
            return if (this == 0) 0 else (1..this).reduce { acc, i -> acc + i }
        }
        return computeSteps(input) { lhs, rhs -> abs(lhs - rhs).steps() }
    }

    val input = readText("Day07").split(',').map { it.trim().toInt() }
    val inputTest = listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)

    check(part1(inputTest) == 37)
    check(part2(inputTest) == 168)

    println(part1(input))
    println(part2(input))
}
