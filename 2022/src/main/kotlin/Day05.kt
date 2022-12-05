
import java.util.*

private const val CHUNK_SIZES = 4
private const val PART_ONE_EXPECTED = "CMZ"
private const val PART_TWO_EXPECTED = "MCD"

private enum class Mover {
    LEGACY, LATEST
}

private data class Command(val start: Int, val end: Int, val moves: Int) {
    companion object {
        fun parse(string: String): Command {
            return string
                .replace(mapOf("move" to "", "from" to "", "to" to ""))
                .trim()
                .split("\\s+".toRegex())
                .let { arr -> Command(arr[1].toInt() - 1, arr[2].toInt() - 1, arr[0].toInt()) }
        }
    }
}

fun main() {
    fun part1(input: List<String>): String = compute(input, Mover.LEGACY)
    fun part2(input: List<String>): String = compute(input, Mover.LATEST)

    val input = readLines("Day05")
    val inputTest = readLines("Day05-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}

private fun String.crates(): List<Char> = buildList {
    for (i in (1..this@crates.lastIndex step CHUNK_SIZES))
        add(this@crates[i])
}

private fun compute(input: List<String>, mover: Mover): String {
    val (cmdList, crateList) = parse(input)
    cmdList.forEach { (from, to, moves) ->
        (0 until moves)
            .map { _ -> crateList[from].pop() }
            .let { if (mover == Mover.LEGACY) it else it.reversed() }
            .forEach { crateList[to].push(it) }
    }

    return crateList.fold("") { acc, chars -> acc + chars.peek() }
}

private fun parse(input: List<String>): Pair<List<Command>, List<Stack<Char>>> {
    val idx = input.indexOfFirst { it.substring(0, 2) == " 1" }
    val max = input[idx].dropLastWhile { it == ' ' }.last().code - '0'.code
    val crateStacks = (0 until max).map { _ -> Stack<Char>() }

    input.subList(0, idx)
        .reversed()
        .map { it.crates() }
        .forEach { crates ->
            crates.forEachIndexed { idx, crate ->
                if (crate != ' ') crateStacks[idx].push(crate)
            }
        }

    return input.subList(idx + 2, input.size) .map(Command.Companion::parse) to crateStacks
}
