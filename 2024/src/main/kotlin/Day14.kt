
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.math.geometry.IntPoint
import com.github.leokash.adventofcode.utils.matrix.*

private const val PART_ONE_EXPECTED = 12

private val xmas_tree_top = listOf(
    listOf(' ',' ','*',' ',' '),
    listOf(' ','*','*','*',' '),
    listOf('*','*','*','*','*')
)

private fun List<String>.mapInts(): List<Int> = map { it.toInt() }
private data class Robot(var position: IntPoint, private val maxX: Int, private val maxY: Int, val velocity: IntPoint) {
    private val xCounter = CircularCounter(max = maxX, start = position.x)
    private val yCounter = CircularCounter(max = maxY, start = position.y)

    fun move() {
        xCounter.set(position.x + velocity.x)
        yCounter.set(position.y + velocity.y)
        position = IntPoint(xCounter.get(),yCounter.get())
    }
}

fun main() {
    data class State(val t: Int, val p: IntPoint, val v: IntPoint)
    fun parse(input: List<String>): Triple<Int, Int, List<Robot>> {
        val numbers = input.map {
            it.findAll(numberRegex).mapInts()
        }

        val maxX = numbers.maxOf { it[1] }
        val maxY = numbers.maxOf { it[0] }
        return Triple(maxX, maxY, numbers.map { Robot(IntPoint(it[1], it[0]), maxX, maxY, IntPoint(it[3], it[2])) })
    }
    fun simulate(time: Int, robots: List<Robot>, cache: MutableMap<State, IntPoint>) {
        var t = 0
        while (t != time) {
            t++
            robots.onEach {
                cache.getOrPut(State(t, it.position, it.velocity)) {
                    it.move()
                    it.position
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val (maxX, maxY, robots) = parse(input)
        val cache = mutableMapOf<State, IntPoint>()

        simulate(100, robots, cache)
        val groups = robots.groupBy { it.position }
        val matrix = IntMatrix(maxX + 1, maxY + 1) { x, y -> groups[IntPoint(x , y)]?.size ?: 0 }

        val midX = maxX / 2
        val midY = maxY / 2
        val topLQuadrant = matrix.slice(IntPoint(0, 0), midX, midY)?.sumOf { (_, n) -> n } ?: 0
        val topRQuadrant = matrix.slice(IntPoint(0, midY + 1), midX, midY)?.sumOf { (_, n) -> n } ?: 0
        val bottomLQuadrant = matrix.slice(IntPoint(midX + 1, 0), midX, midY)?.sumOf { (_, n) -> n } ?: 0
        val bottomRQuadrant = matrix.slice(IntPoint(midX + 1, midY + 1), midX, midY)?.sumOf { (_, n) -> n } ?: 0

        return topLQuadrant * topRQuadrant * bottomLQuadrant * bottomRQuadrant
    }
    fun part2(input: List<String>): Int {
        var time = 0
        val (maxX, maxY, robots) = parse(input)
        val cache = mutableMapOf<State, IntPoint>()
        val matrix = Matrix.ofChars(maxX + 1, maxY + 1)

        do {
            time++
            var foundTree = false
            simulate(1, robots, cache)
            val points = robots.map { it.position }.toSet()
            for ((p, _) in matrix)
                matrix[p] = if (p in points) '*' else ' '

            matrix.scan(3, 5) { p, stop, list ->
                if (list == xmas_tree_top) {
                    foundTree = true
                    stop.flag = true
                }
            }
        } while (!foundTree && time < 10_000)

        return time
    }

    val input = readLines("Day14")
    val inputTest = readLines("Day14-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))
    println(part2(input))
}
