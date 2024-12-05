
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.chunkedBy

private const val PART_ONE_EXPECTED = 143
private const val PART_TWO_EXPECTED = 123

private fun List<Int>.badIndex(rules: Map<Int, Set<Int>>): Int? {
    for (i in 1..<size) {
        val precedingPages = rules[get(i)]
        if (precedingPages == null || precedingPages.intersect(take(i).toSet()).size != i)
            return i
    }

    return null
}

private fun List<Int>.fixUpdate(rules: Map<Int, Set<Int>>): List<Int> {
    val newUpdates = toMutableList()
    while (true) {
        val idx = newUpdates.badIndex(rules) ?: break
        val precedingPages = rules[newUpdates[idx]]
        if (precedingPages == null || precedingPages.intersect(newUpdates.take(idx).toSet()).size != idx)
            newUpdates
                .indexOfFirst { val set = rules[it]; set != null && newUpdates[idx] in set }
                .let { j -> newUpdates[idx] = newUpdates[j].also { newUpdates[j] = newUpdates[idx] } }
    }

    return newUpdates
}

private fun parse(input: List<String>): Pair<Map<Int, Set<Int>>, List<List<Int>>> {
    fun List<List<Int>>.group(): Map<Int, Set<Int>> {
        return buildMap {
            for (rule in this@group) {
                (getOrPut(rule[1]) { mutableSetOf() } as MutableSet<Int>).add(rule[0])
            }
        }
    }

    return input
        .chunkedBy { it.isBlank() }
        .let {
            Pair(
                it[0].map { rule -> rule.split("|").map { n -> n.toInt() } }.group(),
                it[1].map { line -> line.split(",").map { n -> n.toInt() } }
            )
        }
}

fun main() {
    fun part1(input: List<String>): Int {
        return parse(input).let { (rules, updates) ->
            updates
                .filter { it.badIndex(rules) == null }
                .sumOf { it[it.size / 2] }
        }
    }
    fun part2(input: List<String>): Int {
        return parse(input).let { (rules, updates) ->
            updates
                .filter { it.badIndex(rules) != null }
                .map { it.fixUpdate(rules) }
                .sumOf { it[it.size / 2] }
        }
    }

    val input = readLines("Day05")
    val inputTest = readLines("Day05-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
