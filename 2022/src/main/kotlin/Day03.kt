
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
    fun findCommon(lhs: CharArray, rhs: CharArray): Char {
        for (lc in lhs)
            for (rc in rhs)
                if (lc == rc) return lc
        error("!!")
    }

    return input
        .map { it.chunked(it.length / 2) }
        .map { findCommon(it[0].toCharArray(), it[1].toCharArray()) }
        .sumOf { it.priority }
}

private fun computeTwo(input: List<String>, chunks: Int = 3): Int {
    fun findCommon(set1: String, set2: String, set3: String): Char {
        for (c1 in set1.toSet())
            for (c2 in set2.toSet())
                for (c3 in set3.toSet())
                    if (c1 == c2 && c1 == c3) return c1
        error("!!")
    }

    return input
        .chunked(chunks)
        .map { findCommon(it[0], it[1], it[2]) }
        .sumOf { it.priority }
}
