import com.github.leokash.adventofcode.utils.readLines

private fun List<String>.count(col: Int, transform:(Pair<Int, Int>) -> Char): Char {
    return map { it[col] }
        .fold(Pair(0,0)) { (ones, zeros), c -> if (c == '1') ones+1 to zeros else ones to zeros+1 }
        .let(transform)
}

private fun compute1(input: List<String>, transform: (Pair<Int, Int>) -> Char): Int {
    return (0 until input[0].length)
        .map { input.count(it, transform) }
        .joinToString(separator = "")
        .toInt(2)
}
private fun compute2(input: List<String>, transform: (Pair<Int, Int>) -> Char): Int {
    return (0 until input[0].length)
        .foldIndexed(input) { i, arr, _ ->
            if (arr.size == 1)
                arr
            else {
                val bit = arr.count(i, transform)
                arr.filter { it[i] == bit }
            }
        }[0].toInt(2)
}

fun main() {
    val mcb: (Pair<Int, Int>) -> Char = { (ones, zeros) -> if (ones >= zeros) '1' else '0' }
    val lcb: (Pair<Int, Int>) -> Char = { (ones, zeros) -> if (zeros <= ones) '0' else '1' }

    fun part1(input: List<String>): Int {
        return compute1(input, mcb) * compute1(input, lcb)
    }
    fun part2(input: List<String>): Int {
        return compute2(input, mcb) * compute2(input, lcb)
    }

    val input = readLines("Day03")
    val inputTest = readLines("Day03-Test")

    check(part1(inputTest) == 198)
    check(part2(inputTest) == 230)

    println(part1(input))
    println(part2(input))
}
