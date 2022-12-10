
@file:Suppress("MagicNumber")

private const val PART_ONE_EXPECTED = 13140

private sealed class Instruction(val cycles: Int) {
    object Noop: Instruction(1)
    data class Add(val x: Int): Instruction(2)
}

private val String.instruction: Instruction get() {
    return if (this == "noop") Instruction.Noop else Instruction.Add(split(" ")[1].toInt())
}

fun main() {
    val cycles = (20..220 step 40)
    fun compute(input: List<String>, crt: List<MutableList<Char>>? = null): Int {
        var reg = 1
        var cycle = 0
        var sprite = 0..2
        return input
            .sumOf { string ->
                var sum = 0
                val ins = string.instruction
                repeat(ins.cycles) {
                    val pos = (cycle++).toPoint(40, 40)
                    if (cycle in cycles) sum = reg * cycle
                    if (pos.y in sprite) crt?.let { it[pos.x][pos.y] = SPRITE_ON }
                }

                if (ins is Instruction.Add) {
                    reg += ins.x
                    sprite = (reg - 1)..(reg + 1)
                }
                sum
            }
    }

    fun part1(input: List<String>): Int {
        return compute(input)
    }
    fun part2(input: List<String>): String {
        val crt = buildList {
            repeat(6) {
                add(MutableList(40) { SPRITE_OFF })
            }
        }

        compute(input, crt)
        return crt.joinToString(separator = "") { line ->
            line.joinToString(separator = "", postfix = "\n")
        }
    }

    val input = readLines("Day10")
    val inputTest = readLines("Day10-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    println(part2(inputTest))
    println(part2(input))
}
