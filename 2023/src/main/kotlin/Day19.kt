
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.chunkedBy
import com.github.leokash.adventofcode.utils.collections.product
import com.github.leokash.adventofcode.utils.math.context.Context
import kotlin.math.max
import kotlin.math.min

private const val PART_ONE_EXPECTED = 19114L
private const val PART_TWO_EXPECTED = 167409079868000L

private data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    val score: Int get() = x + m + a + s

    companion object {
        val rgx = """x=(\d+),m=(\d+),a=(\d+),s=(\d+)""".toRegex()

        operator fun invoke(string: String): Part {
            return string
                .matchingGroups(rgx)
                .map { it.toInt() }
                .run { Part(this[0], this[1], this[2], this[3]) }
        }
    }
}
private data class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
    val score: Long get() = listOf(x, m, a, s).product(Context<Long>()) { it.last - it.first + 1L }
}

private data class Rule(val name: String, val conditions: List<Condition>) {
    sealed interface Condition {
        val next: String
        fun evaluate(part: Part): String?

        fun Part.evaluate(key: String, comparator: (Int) -> Boolean): Boolean {
            when (key) {
                "x" -> if (comparator(x)) return true
                "m" -> if (comparator(m)) return true
                "a" -> if (comparator(a)) return true
                "s" -> if (comparator(s)) return true
            }

            return false
        }

        data class Redirect(override val next: String): Condition {
            override fun evaluate(part: Part) = next
        }
        data class LessThan(override val next: String, val key: String, val value: Int): Condition {
            override fun evaluate(part: Part): String? {
                return if (part.evaluate(key) { it < value }) return next else null
            }
        }
        data class GreaterThan(override val next: String, val key: String, val value: Int): Condition {
            override fun evaluate(part: Part): String? {
                return if (part.evaluate(key) { it > value }) return next else null
            }
        }

        companion object {
            private val rgx = """(\w)([<>])(\d+)""".toRegex()

            operator fun invoke(string: String): Condition {
                if (":" !in string) {
                    return Redirect(string)
                }

                val (cmd, next) = string.split(":")
                val (key, op, value) = cmd.matchingGroups(rgx)
                return when (op) {
                    "<" -> LessThan(next, key, value.toInt())
                    else -> GreaterThan(next, key, value.toInt())
                }
            }
        }
    }

    fun evaluate(part: Part): String = conditions.firstNotNullOf { it.evaluate(part) }

    companion object {
        operator fun invoke(string: String): Rule {
                return string.split("{").let { arr ->
                    Rule(arr[0], arr[1].replace("}", "").let { it.split(",").map { c -> Condition(c) } })
                }
        }
    }
}

private fun Rule.Condition.evaluate(part: PartRange): Pair<Pair<String, PartRange>, PartRange> {
    fun IntRange.split(middle: Int, lessThan: Boolean): Pair<IntRange, IntRange> {
        return when (lessThan) {
            true -> start .. min(middle - 1, endInclusive) to max(start, middle) .. endInclusive
            else -> max(middle + 1, start) .. endInclusive to start .. min(middle, endInclusive)
        }
    }

    fun evaluate(key: String, middle: Int, lessThan: Boolean): Pair<PartRange, PartRange> {
        return when (key) {
            "x" -> part.x.split(middle, lessThan).let { (pass, rem) -> part.copy(x = pass) to part.copy(x = rem) }
            "m" -> part.m.split(middle, lessThan).let { (pass, rem) -> part.copy(m = pass) to part.copy(m = rem) }
            "a" -> part.a.split(middle, lessThan).let { (pass, rem) -> part.copy(a = pass) to part.copy(a = rem) }
            "s" -> part.s.split(middle, lessThan).let { (pass, rem) -> part.copy(s = pass) to part.copy(s = rem) }

            else -> error("invalid part key: $key")
        }
    }

    return when (this) {
        is Rule.Condition.Redirect -> (next to part) to part
        is Rule.Condition.LessThan -> evaluate(key, value, true).let { (accept, reject) -> (next to accept) to reject}
        is Rule.Condition.GreaterThan -> evaluate(key, value, false).let { (accept, reject) -> (next to accept) to reject}
    }
}

fun main() {
    Logger.debug = true
    fun parse(input: List<String>): Pair<List<Rule>, List<Part>> {
        return input
            .chunkedBy { it.isEmpty() }
            .let { it[0].map { s -> Rule(s) } to it[1].map { s -> Part(s) } }
    }

    fun part1(input: List<String>): Long {
        fun evaluate(part: Part, map: Map<String, Rule>): Boolean {
            var rule = map.getValue("in")
            while (true) {
                when (val next = rule.evaluate(part)) {
                    "R" -> return false
                    "A" -> return true
                    else -> rule = map.getValue(next)
                }
            }
        }

        return with(parse(input)) {
            val map = first.associateBy { it.name }
            second.filter { evaluate(it, map) }.sumOf { it.score.toLong() }
        }
    }

    fun part2(input: List<String>): Long {
        with(parse(input)) {
            var sum = 0L
            val rules = first.associateBy { it.name }
            val queue = ArrayDeque<Pair<String, PartRange>>().apply {
                add("in" to PartRange(1..4000, 1..4000, 1..4000, 1..4000))
            }

            while (queue.isNotEmpty()) {
                val (key, part) = queue.removeFirst()
                when (key) {
                    "R" -> continue
                    "A" -> sum += part.score
                    else -> {
                        var tmp = part
                        val rule = rules.getValue(key)
                        for (c in rule.conditions) {
                            val (res, rem) = c.evaluate(tmp)
                            queue.add(res)
                            tmp = rem
                        }
                    }
                }
            }

            return sum
        }
    }

    val input = readLines("Day19")
    val inputTest = readLines("Day19-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
