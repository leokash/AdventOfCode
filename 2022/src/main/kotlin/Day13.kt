
@file:Suppress("all")

import kotlin.math.min

private const val PART_ONE_EXPECTED = 13
private const val PART_TWO_EXPECTED = 140

private sealed class Packet: Comparable<Packet> {
    abstract fun grouping(): Grouping

    class Single(val value: Int): Packet() {
        override fun grouping(): Grouping {
            return Grouping(listOf(this))
        }

        override fun compareTo(other: Packet): Int {
            return when (other) {
                is Single -> value.compareTo(other.value)
                else -> grouping().compareTo(other)
            }
        }
    }
    class Grouping(val packets: List<Packet> = emptyList()): Packet() {
        override fun grouping(): Grouping {
            return this
        }
        override fun compareTo(other: Packet): Int {
            val rhs = other.grouping()
            for (i in (0 until min(packets.size, rhs.packets.size))) {
                val result = packets[i].compareTo(rhs.packets[i])
                if (result != 0) return result
            }

            return packets.size.compareTo(rhs.packets.size)
        }
    }

    companion object {
        private fun parse(string: String): Packet {
            fun parseDigit(c: Int): Pair<Int, Packet> {
                return if (string[c + 1] in '0'..'9')
                    (c + 2) to Single(string.substring(c, c + 2).toInt())
                else
                    (c + 1) to Single(string[c].digitToInt())
            }
            fun parseGrouping(cursor: Int): Pair<Int, Packet> {
                var cur = cursor
                val children = mutableListOf<Packet>()
                while (true) {
                    when (string[cur]) {
                        ',' -> cur++
                        ']' -> break
                        '[' -> { parseGrouping(cur + 1).also { (i, child) -> cur = i; children.add(child) } }
                        in '0'..'9' -> { parseDigit(cur).also { (i, child) -> cur = i; children.add(child) } }
                    }
                }

                return (cur + 1) to Grouping(children)
            }

            return parseGrouping(1).second
        }
        fun parse(input: List<String>): List<Packet> {
            return input.asSequence()
                .filter { it.isNotBlank() }
                .map { parse(it) }
                .toList()
        }
    }
}

fun main() {
    Logger.debug = true
    fun compute1(input: List<String>): Int {
        return Packet.parse(input)
            .chunked(2)
            .mapIndexed { idx, (lhs, rhs) -> if (lhs < rhs) (idx + 1) else 0 }
            .sum()
    }

    fun compute2(input: List<String>): Int {
        val div1 = Packet.parse(listOf("[[2]]")).first()
        val div2 = Packet.parse(listOf("[[6]]")).first()
        val packets = (Packet.parse(input) + div1 + div2).sorted()
        return (packets.indexOf(div1) + 1) * (packets.indexOf(div2) + 1)
    }

    fun part1(input: List<String>): Int = compute1(input)
    fun part2(input: List<String>): Int = compute2(input)

    val input = readLines("Day13")
    val sample = readLines("Day13-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}
