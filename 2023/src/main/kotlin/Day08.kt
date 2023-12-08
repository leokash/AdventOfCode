
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.context.lcm

private const val PART_ONE_EXPECTED_A = 2L
private const val PART_ONE_EXPECTED_B = 6L
private const val PART_TWO_EXPECTED = 6L

private val partOneExample = """LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
""".trimIndent()

private val partTwoExample = """LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
""".trimIndent()

private data class Node(val name: String, val links: List<String>)

fun main() {
    fun steps(start: String, dir: List<Char>, map: Map<String, Node>, predicate: (String) -> Boolean): Long {
        var steps = 0L
        var current = start
        val counter = CircularCounter(0, dir.lastIndex)
        while (!predicate(current)) {
            steps++
            current = map.getValue(current).links[dir[counter.getAndIncrement()].let { if (it == 'L') 0 else 1 }]
        }

        return steps
    }

    fun part1(input: List<String>): Long {
        val (dir, nodes) = parse(input)
        return steps("AAA", dir, nodes) { it == "ZZZ" }
    }

    fun part2(input: List<String>): Long {
        val ctx = Context<Long>()
        return parse(input).let { (dir, nodes) ->
            nodes.keys.filter { it[2] == 'A' }.map { cur ->
                steps(cur, dir, nodes) { it.endsWith('Z') }
            }
        }.reduce { acc, num -> ctx.lcm(acc, num) }
    }

    val input = readLines("Day08")
    val inputTest = readLines("Day08-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED_A)
    check(part1(partOneExample.split("\n")) == PART_ONE_EXPECTED_B)
    check(part2(partTwoExample.split("\n")) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}

private val regex = """(\w{3}) = \((\w{3}), (\w{3})\)""".toRegex()

private fun parse(input: List<String>): Pair<List<Char>, Map<String, Node>> {
    return input[0].trim().toList() to input.drop(2).associate {
        it.matchingGroups(regex).let { (name, left, right) ->
            name to Node(name, listOf(left, right))
        }
    }
}
