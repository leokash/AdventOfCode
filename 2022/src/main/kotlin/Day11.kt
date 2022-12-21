
@file:Suppress("MagicNumber")

import Monkey.Companion.parse
import Operation.Companion.toOperation
import com.github.leokash.adventofcode.utils.readLines

private const val PART_ONE_EXPECTED = 10605L
private const val PART_TWO_EXPECTED = 2713310158L

private data class Monkey(
    val id: String,
    val divisor: Long,
    val items: MutableList<Long>,
    val onTrue: String,
    val onFalse: String,
    val operation: Operation
) {
    var inspections: Long = 0
    fun inspect(monkeys: Map<String, Monkey>, pacifier: (Long) -> Long) {
        while (items.isNotEmpty()) {
            inspections++
            val value = pacifier(operation(items.removeFirst()))
            monkeys[if (value % divisor == 0L) onTrue else onFalse]?.items?.add(value)
        }
    }

    companion object {
        fun List<String>.parse(): List<Monkey> {
            return chunked(7) { m ->
                Monkey(
                    m[0].substringAfter("Monkey ").dropLast(1),
                    m[3].substringAfter("Test: divisible by ").toLong(),
                    m[1].substringAfter("Starting items: ").split(", ").map { it.toLong() }.toMutableList(),
                    m[4].substringAfter("If true: throw to monkey "),
                    m[5].substringAfter("If false: throw to monkey "),
                    m[2].substringAfter("Operation: new = ").toOperation()
                )
            }
        }
    }
}
private sealed class Operation {
    abstract operator fun invoke(lhs: Long): Long

    object Square: Operation() {
        override fun invoke(lhs: Long): Long = lhs * lhs
    }
    data class Plus(private val rhs: Long): Operation() {
        override fun invoke(lhs: Long): Long = lhs + rhs
    }
    data class Times(private val rhs: Long): Operation() {
        override fun invoke(lhs: Long): Long = lhs * rhs
    }

    companion object {
        fun String.toOperation(): Operation {
            val (lhs, sym, rhs) = split(" ")
            return when {
                lhs == rhs -> Square
                sym == "+" -> Plus(rhs.toLong())
                sym == "*" -> Times(rhs.toLong())

                else -> error("Illegal operation requested for: '$this'")
            }
        }
    }
}

fun main() {
    fun compute(rounds: Int, monkeys: Map<String, Monkey>, pacifier: (Long) -> Long): Long {
        return monkeys
            .also { map -> repeat(rounds) { map.values.forEach { it.inspect(map, pacifier) } } }
            .values
            .sortedByDescending { it.inspections }
            .take(2)
            .let { it[0].inspections * it[1].inspections }
    }

    fun part1(input: List<String>): Long {
        return compute(20, input.parse().associateBy { it.id }) { it.floorDiv(3L) }
    }
    fun part2(input: List<String>): Long {
        val monkeys = input.parse()
        val pacifierMod = monkeys.map { it.divisor }.reduce(Long::times)
        return compute(10000, monkeys.associateBy { it.id }) { it % pacifierMod }
    }

    val input = readLines("Day11")
    val inputTest = readLines("Day11-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
