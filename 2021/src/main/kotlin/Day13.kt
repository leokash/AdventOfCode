
private data class Instruction(val axis: String, val position: Int)

private fun Set<Point>.print() {
    for (y in (0 .. maxOf { it.y })) {
        for (x in (0 .. maxOf { it.x })) {
            print(if (contains(x, y)) "#" else " ")
        }
        println()
    }
}
private fun Set<Point>.contains(x: Int, y: Int): Boolean {
    return any { it.x == x && it.y == y }
}
private fun fold(point: Point, instruction: Instruction): Point {
    val (axis, index) = instruction
    val pos = if (axis == "x") point.x else point.y
    if (pos < index) return point.copy()
    return if (axis == "x") point.copy(x = 2 * index - pos) else point.copy(y = 2 * index - pos)
}

fun main() {

    fun parse(input: List<String>): Pair<List<Instruction>, Set<Point>> {
        val commands = mutableListOf<Instruction>()
        val coordinates = mutableSetOf<Point>()
        for (line in input) {
            if (line.isEmpty())
                continue
            if (line.startsWith("fold")) {
                val (axis, pos) = line.trim().replace("fold along", "").trim().split('=')
                commands += Instruction(axis, pos.toInt())
            } else {
                val (x, y) = line.trim().split(',')
                coordinates += Point(x.toInt(), y.toInt())
            }
        }


        return commands to coordinates
    }

    fun part1(input: List<String>): Int {
        val (cmd, coordinates) = parse(input)
        return coordinates.map { fold(it, cmd.first()) }.toSet().size.also { println("points = $it") }
    }
    fun part2(input: List<String>): Int {
        val (commands, coordinates) = parse(input)
        var result = coordinates
        for (cmd in commands)
            result = result.map { fold(it, cmd) }.toSet()
        result.print()
        return 0
    }

    val input = readLines("Day13")
    val inputTest = readLines("Day13-Test")

    check(part1(inputTest) == 17)
    check(part2(inputTest) == 0)

    println(part1(input))
    println(part2(input))
}
