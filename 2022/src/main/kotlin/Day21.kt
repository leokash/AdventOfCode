
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 152L
private const val PART_TWO_EXPECTED = 301L

fun main() {
    Logger.debug = true
    fun computeOne(input: List<String>): Long {
        return MonkeyBroker(parse(input)).yell("root")
    }

    fun computeTwo(input: List<String>): Long {
        fun MonkeyOperator.reversedYell(lhs: Long, rhs: Long, isRight: Boolean = true): Long {
            return when (op) {
                '+' -> lhs - rhs
                '*' -> lhs / rhs
                '-' -> if (isRight) rhs - lhs else lhs + rhs
                '/' -> if (isRight) rhs / lhs else lhs * rhs
                else -> error("invalid operation requested: $op")
            }
        }

        fun findHumanNumber(path: String, needed: Long, broker: MonkeyBroker): Long {
            if (path == "humn") return needed
            val monkey = broker.operator(path) ?: return 0
            val lhsNumber = broker.monkey(monkey.lhs).tentativeYell(broker)
            val rhsNumber = broker.monkey(monkey.rhs).tentativeYell(broker)
            val newNeeded = monkey.reversedYell(needed, lhsNumber ?: rhsNumber ?: 0, lhsNumber != null)
            return findHumanNumber(if (lhsNumber == null) monkey.lhs else monkey.rhs, newNeeded, broker)
        }

        val broker = MonkeyBroker(parse(input))
        val (_, lhs, rhs) = broker.operator("root") ?: return 0

        val lhsNumber = broker.tentativeYell(lhs)
        val rhsNumber = broker.tentativeYell(rhs)
        return findHumanNumber(if (lhsNumber == null) lhs else rhs, lhsNumber ?: rhsNumber ?: 0, broker)
    }

    fun part1(input: List<String>) = computeOne(input)
    fun part2(input: List<String>) = computeTwo(input)

    val input = readLines("Day21")
    val sample = readLines("Day21-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}

private interface IMonkey {
    val name: String

    fun yell(broker: MonkeyBroker): Long
    fun tentativeYell(broker: MonkeyBroker): Long?
}
private data class MonkeyNumber(override val name: String, val number: Long): IMonkey {
    override fun yell(broker: MonkeyBroker): Long {
        return number
    }
    override fun tentativeYell(broker: MonkeyBroker): Long? {
        return if (name != "humn") number else null
    }
}
private data class MonkeyOperator(override val name: String, val lhs: String, val rhs: String, val op: Char): IMonkey {
    override fun yell(broker: MonkeyBroker): Long {
        return compute(broker.yell(lhs), broker.yell(rhs))
    }
    override fun tentativeYell(broker: MonkeyBroker): Long? {
        broker.tentativeYell(lhs)?.let { num1 ->
            broker.tentativeYell(rhs)?.let { num2 ->
                return compute(num1, num2)
            }
        }

        return null
    }

    private fun compute(lhsNum: Long, rhsNum: Long): Long {
        return when (op) {
            '/' -> lhsNum / rhsNum
            '+' -> lhsNum + rhsNum
            '-' -> lhsNum - rhsNum
            '*' -> lhsNum * rhsNum
            else -> error("invalid operation requested: $op")
        }
    }
}

private class MonkeyBroker(monkeys: List<IMonkey>) {
    private val cache = mutableMapOf<String, Long>()
    private val monkeys = monkeys.associateBy { it.name }

    fun yell(monkey: String): Long {
        return cache.getOrPut(monkey) { monkey(monkey).yell(this) }
    }
    fun tentativeYell(monkey: String): Long? {
        if (monkey in cache) return cache.getValue(monkey)
        return monkey(monkey).tentativeYell(this)?.also { cache[monkey] = it }
    }

    fun monkey(name: String): IMonkey {
        return monkeys.getValue(name)
    }
    fun operator(monkey: String): MonkeyOperator? {
        return monkeys[monkey] as? MonkeyOperator
    }
}

private val regex = Regex("""\w+: \d+$""")
private fun parse(input: List<String>): List<IMonkey> {
    return input.map { string ->
        if (string matches regex)
            string.split(": ").let { MonkeyNumber(it[0], it[1].toLong()) }
        else
            string.split(": ").let { p -> p[1].split(" ").let { MonkeyOperator(p[0], it[0], it[2], it[1][0]) } }
    }
}
