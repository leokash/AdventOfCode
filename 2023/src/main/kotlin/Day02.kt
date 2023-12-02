
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 8
private const val PART_TWO_EXPECTED = 2286

private const val MAX_RED_CUBES = 12
private const val MAX_BLUE_CUBES = 14
private const val MAX_GREEN_CUBES = 13

fun main() {
    fun part1(input: List<String>): Int = input.sumOf {
        fun String.valid(): Boolean {
            val (cubes, color) = this.split(" ")
            return when (color) {
                "red" -> cubes.toInt() <= MAX_RED_CUBES
                "blue" -> cubes.toInt() <= MAX_BLUE_CUBES
                "green" -> cubes.toInt() <= MAX_GREEN_CUBES

                else -> error("invalid color provided: $color")
            }
        }

        val draws = it.findAll("""(\d{2} \w+)""".toRegex())
        val gameId = it.substring(5, it.indexOf(':')).toInt()
        if (draws.isEmpty() || draws.all { s -> s.valid() }) gameId else 0
    }

    fun part2(input: List<String>): Int = input.sumOf { game ->
        fun String.max(cube: String): Int {
            val (amount, color) = this.split(" ")
            return if (cube == color) amount.toInt() else 0
        }

        val draws = game.findAll("""(\d+ \w+)""".toRegex())
        draws.maxOf { it.max("red") } * draws.maxOf { it.max("blue") } * draws.maxOf { it.max("green") }
    }

    val input = readLines("Day02")
    val inputTest = readLines("Day02-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
