import com.github.leokash.adventofcode.utils.readLines

private fun countChars(last: Char, groups: Map<String, Long>): Map<Char, Long> {
    return mutableMapOf(last to 1L).apply {
        groups.forEach { (pair, count) ->
            put(pair[0], (this[pair[0]] ?: 0) + count)
        }
    }
}
private fun simulate(start: String, steps: Int, polymerRules: Map<String, String>): Long {
    var groups = start.windowed(2).groupBy { it }.entries.associate { (k, v) -> k to v.size.toLong() }
    repeat(steps) {
        groups = buildMap {
            groups.forEach { (key, count) ->
                polymerRules[key]?.let { middle ->
                    val lhs = "${key[0]}$middle"
                    val rhs = "$middle${key[1]}"
                    put(lhs, (this[lhs] ?: 0) + count)
                    put(rhs, (this[rhs] ?: 0) + count)
                }
            }
        }
    }

    return countChars(start.last(), groups).let { count ->
        count.maxOf { it.value } - count.minOf { it.value }
    }
}

fun main() {
    fun parse(input: List<String>): Pair<String, Map<String, String>> {
        return Pair(
            input[0].trim(),
            input.drop(2).map { it.trim().split(" -> ") }.associate { it[0] to it[1] }
        )
    }

    fun part1(input: List<String>): Long {
        return parse(input).let { (start, map) -> simulate(start, 10, map) }
    }
    fun part2(input: List<String>): Long {
        return parse(input).let { (string, map) -> simulate(string, 40, map) }
    }

    val input = readLines("Day14")
    val inputTest = readLines("Day14-Test")

    check(part1(inputTest) == 1588L)
    check(part2(inputTest) == 2188189693529)

    println(part1(input))
    println(part2(input))
}
