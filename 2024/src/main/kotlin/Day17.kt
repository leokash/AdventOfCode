
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.mapLongs
import kotlin.Long
import kotlin.math.pow

private const val PART_ONE_EXPECTED = "4,6,3,5,6,3,5,2,1,0"
private const val PART_TWO_EXPECTED = 117440L

private val bst: (Long) -> Long = { it % 8 }
private val adv: (Long, Long) -> Long = { a, b -> a / b }
private val bxl: (Long, Long) -> Long = { a, b -> a xor b }
private val jnz: (Long, Long) -> Int? = { a, b -> if (a == 0L) null else b.toInt() }

data class Computer(var ip: Int = 0, var rA: Long = 0, var rB: Long = 0, var rC: Long = 0) {
    fun run(program: List<Long>, probe: (Long) -> Unit = { }): String {
        return buildList {
            while (ip < program.size) {
                val op = program[ip]
                when (op) {
                    0L -> rA = adv(rA, pow(fetch(program[ip + 1]))).also { ip += 2; probe(rA) }
                    1L -> rB = bxl(rB, program[ip + 1]).also { ip += 2 }
                    2L -> rB = bst(fetch(program[ip + 1])).also { ip += 2 }
                    3L -> ip = jnz(rA, program[ip + 1]) ?: (ip + 2)
                    4L -> rB = bxl(rB, rC).also { ip += 2 }
                    5L -> add(bst(fetch(program[ip + 1]))).also { ip += 2 }
                    6L -> rB = adv(rA, pow(fetch(program[ip + 1]))).also { ip += 2 }
                    7L -> rC = adv(rA, pow(fetch(program[ip + 1]))).also { ip += 2 }
                }
            }
        }
            .joinToString(",")
    }

    private fun pow(exp: Long): Long {
        return 2.0.pow(exp.toDouble()).toLong()
    }

    private fun fetch(num: Long): Long {
        return when(num) {
            4L -> rA
            5L -> rB
            6L -> rC
            else -> num
        }
    }
}

fun main() {
    Logger.enabled = true
    fun part1(input: List<String>): String {
        val computer = Computer()
        computer.rA = input[0].findAll(numberRegex)[0].toLong()
        computer.rB = input[1].findAll(numberRegex)[0].toLong()
        computer.rC = input[2].findAll(numberRegex)[0].toLong()

        return computer.run(input[4].findAll(numberRegex).map { it.toLong() })
    }
    fun part2(input: List<String>): Long {
        val cmptr = Computer()
        val program = input[4].findAll(numberRegex).map(String::toLong)

        val stack = mutableListOf(0L to program.lastIndex)
        while (stack.isNotEmpty()) {
            val (tmp, size) = stack.removeFirst()
            for (i in 0..7) {
                val a = tmp * 8L + i
                val result = cmptr
                    .apply { ip = 0; rA = a }
                    .run(program)
                    .mapLongs(commaRegex)

                if (result == program) return a
                if (result == program.subList(size, program.size)) {
                    stack += (a to size - 1)
                }
            }
        }

        return -1L
    }

    val input = readLines("Day17")
    check(part1(readLines("Day17-Test-A")) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(readLines("Day17-Test-B")) == PART_TWO_EXPECTED)
    println(part2(input))
}
