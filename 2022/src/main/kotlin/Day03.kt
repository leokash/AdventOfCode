
private const val PART_ONE_EXPECTED = 157
private const val PART_TWO_EXPECTED = 70
private const val LOWERCASE_CHAR_START = 97
private const val UPPERCASE_CHAR_START = 65
private const val LOWERCASE_CHAR_OFFSET = 1
private const val UPPERCASE_CHAR_OFFSET = 27

fun main() {
    fun part1(input: List<String>): Int = computeOne(input)
    fun part2(input: List<String>): Int = computeTwo(input)

    val input = readLines("Day03")
    val inputTest = readLines("Day03-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}

private val Char.priority get() = when(isLowerCase()) {
    true -> (code - LOWERCASE_CHAR_START) + LOWERCASE_CHAR_OFFSET
    else -> (code - UPPERCASE_CHAR_START) + UPPERCASE_CHAR_OFFSET
}

private fun computeOne(input: List<String>): Int {
    fun findCommon(lhs: String, rhs: String): Char {
        return lhs.toSet().intersect(rhs.toSet()).first()
    }

    return input
        .map { it.chunked(it.length / 2).let { list -> findCommon(list[0], list[1]) } }
        .sumOf { it.priority }
}

private fun computeTwo(input: List<String>, chunks: Int = 3): Int {
    fun findCommon(string1: String, string2: String, string3: String): Char {
        return string1.toSet()
            .intersect(string2.toSet())
            .intersect(string3.toSet())
            .first()
    }

    return input
        .chunked(chunks)
        .map { findCommon(it[0], it[1], it[2]) }
        .sumOf { it.priority }
}
