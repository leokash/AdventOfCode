

private fun compute(c: Char, num: Int) = if (c == '(') num + 1 else num - 1

fun main() {

    fun part1(input: CharArray): Int {
        return input.fold(0) { acc, c -> compute(c, acc) }
    }
    fun part2(input: CharArray): Int {
        return input.fold(0 to 0) { (acc, idx), c ->
            if (acc == -1) acc to idx else compute(c, acc) to idx + 1
        }.second
    }

    val input = readText("Day01").toCharArray()

    println(part1(input))
    println(part2(input))
}
