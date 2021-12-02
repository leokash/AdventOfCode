
private val String.asDirection: Direction get() {
    return when (this) {
        "up" -> Direction.North
        "down" -> Direction.South
        "forward" -> Direction.East
        else -> throw RuntimeException("Invalid direction given: $this")
    }
}
private fun List<String>.parse(): List<Pair<Direction, Int>> {
    return map { string ->
        string.split(' ').let { arr ->
            arr[0].trim().asDirection to arr[1].trim().toInt()
        }
    }
}
private fun Point.update(moves: Int, direction: Direction) {
    when(direction) {
        Direction.East -> x += moves
        Direction.North -> y -= moves
        Direction.South -> y += moves
        else -> Unit
    }
}
private data class Vector3(var aim: Int, var x: Int, var y: Int) {
    fun update(moves: Int, direction: Direction) {
        when(direction) {
            Direction.East -> { x += moves; y += (aim * moves) }
            Direction.North -> aim -= moves
            Direction.South -> aim += moves
            else -> Unit
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .parse()
            .fold(Point()) { point, (dir, moves) -> point.apply { update(moves, dir) } }
            .let { it.x * it.y }
    }
    fun part2(input: List<String>): Int {
        return input
            .parse()
            .fold(Vector3(0,0,0)) { point, (dir, moves) -> point.apply { update(moves, dir) } }
            .let { it.x * it.y }
    }

    val input = readLines("Day02")
    val inputTest = readLines("Day02-Test")

    check(part1(inputTest) == 150)
    check(part2(inputTest) == 900)

    println(part1(input))
    println(part2(input))
}
