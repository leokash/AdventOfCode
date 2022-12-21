import com.github.leokash.adventofcode.utils.readLines

private val errorPoints = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
private val autoCompletePoints = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

private fun findErrorChar(line: String): Char? {
    val closing = ArrayList<Char>(10)
    for (c in line)
        if (!parse(c, closing))
            return c
    return null
}
private fun computeAutocompleteScore(line: String): Long? {
    val closing = ArrayList<Char>(10)
    for (c in line)
        if (!parse(c, closing))
            return null

    return closing.let {
        if (it.isEmpty()) null else closing.reversed().fold(0L) { sum, c ->
            sum * 5 + (autoCompletePoints[c] ?: 0)
        }
    }
}
private fun parse(c: Char, closing: ArrayList<Char>): Boolean {
    return when (c) {
        '<' -> closing.add('>')
        '(' -> closing.add(')')
        '[' -> closing.add(']')
        '{' -> closing.add('}')
        else -> {
            val expected = closing.lastOrNull()
            if (expected != null) {
                closing.removeAt(closing.size - 1)
                return c == expected
            }

            return false
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { findErrorChar(it) }
            .groupBy { it }
            .map { (c, arr) -> c to arr.size }
            .sumOf { (c, count) -> (errorPoints[c] ?: 0) * count }
    }
    fun part2(input: List<String>): Long {
        return input
            .mapNotNull { computeAutocompleteScore(it) }
            .sorted()
            .let { arr -> arr[arr.size / 2] }
    }

    val input = readLines("Day10")
    val inputTest = readLines("Day10-Test")

    check(part1(inputTest) == 26397)
    check(part2(inputTest) == 288957L)

    println(part1(input))
    println(part2(input))
}
