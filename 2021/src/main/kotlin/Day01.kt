
fun main() {

    fun part1(input: List<Int>): Int = input.windowed(2).count { it[0] < it[1] }
    fun part2(input: List<Int>): Int = input.windowed(4).count { it[0] < it[3] }

    val input = readLines("Day01").map { it.toInt() }
    val testInput = readLines("Day01-Test").map { it.toInt() }

    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    println(part1(input))
    println(part2(input))
}
