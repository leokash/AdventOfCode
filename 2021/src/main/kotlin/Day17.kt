
import kotlin.math.max

private data class Vec3(val x: Int, val y: Int, val velocity: Int)

private fun compute(xBounds: IntRange, yBounds: IntRange): List<Vec3> {
    return buildList {
        for (x in (0..xBounds.last))
            for (y in (yBounds.first..1000))
                simulate(Point(x, y)) { vx, vy -> vx in xBounds && vy in yBounds}?.let { add(Vec3(x, y, it)) }
    }
}
private fun simulate(velocity: Point, steps: Int = 1000, rangeValidator: (Int, Int) -> Boolean): Int? {
    var max = 0
    var x = 0; var vx = velocity.x
    var y = 0; var vy = velocity.y
    repeat(steps) {
        x += vx
        y += vy
        vy -= 1
        max = max(max, y)
        vx = vx.let { when { it > 0 -> it - 1; it < 0 -> it + 1; else -> 0 } }
        if (rangeValidator(x, y))
            return max
    }

    return null
}

private val numberRegex = """-?\d+""".toRegex()

fun main() {
    fun part1(input: String): Int {
        val (xs, xe, ys, ye) = numberRegex.findAll(input).map { it.value.toInt() }.toList()
        return compute(xs..xe, ys..ye).maxOf { it.velocity }
    }
    fun part2(input: String): Int {
        val (xs, xe, ys, ye) = numberRegex.findAll(input).map { it.value.toInt() }.toList()
        val points = compute(xs..xe, ys..ye)
        return points.distinctBy { Point(it.x, it.y) }.size
    }

    val input = readText("Day17")
    val inputTest = "target area: x=20..30, y=-10..-5"

    check(part1(inputTest) == 45)
    check(part2(inputTest) == 112)

    println(part1(input))
    println(part2(input))
}
