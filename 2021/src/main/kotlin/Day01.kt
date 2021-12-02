
fun main() {

    fun List<Int>.count(): Int {
        return this.comparableFold(0) { acc, lhs, rhs -> if (lhs < rhs) acc + 1 else acc }
    }

    fun part1(input: List<String>): Int = input.map { it.toInt() }.count()
    fun part2(input: List<String>): Int = input.map { it.toInt() }.windowed(3, 1) { it.sum() }.count()

    val input = readLines("Day01")
    val testInput = readLines("Day01-Test")

    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    println(part1(input))
    println(part2(input))
}
