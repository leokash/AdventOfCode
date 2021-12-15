
import java.util.*

private fun <T> Array<Array<T>>.contains(x: Int, y: Int): Boolean {
    return x in indices && y in this[x].indices
}
private operator fun <T> Array<Array<T>>.contains(point: Point): Boolean {
    return contains(point.x, point.y)
}

private fun next(x: Int, y: Int, costs: Array<Array<Int>>): List<Pair<Point, Int>> {
    return listOf(
        Point(x - 1, y),
        Point(x, y - 1),
        Point(x + 1, y),
        Point(x, y + 1)
    ).mapNotNull { p ->
        if (p in costs) p to costs[p.x][p.y] else null
    }
}

private data class Cost(val value: Int, val point: Point): Comparable<Cost> {
    override fun compareTo(other: Cost): Int {
        return value.compareTo(other.value)
    }
}

private fun compute(costs: Array<Array<Int>>): Int {
    val end = Point(costs.size - 1, costs[0].size - 1)
    val map = mutableMapOf<Point, Int>()
    val visited = mutableSetOf<Point>()
    val queue = PriorityQueue<Cost>().also { it.add(Cost(0, Point())) }

    while (queue.isNotEmpty()) {
        val (cost, point) = queue.poll()
        if (point in visited) continue

        visited += point
        map[point] = cost
        if (point == end) break
        next(point.x, point.y, costs).forEach { (p, num) ->
            queue.add(Cost(cost + num, p))
        }
    }

    return map[end] ?: -1
}

private fun printWorld(arr: Array<Array<Int>>) {
    for (x in arr.indices) {
        print("|")
        for (y in arr[x].indices) {
            print(arr[x][y])
        }
        print("|\n")
    }
}

fun main() {
    fun parse(input:List<String>): Array<Array<Int>> {
        return Array(input.size) { x ->
            val numbers = input[x].trim().toList()
            Array(numbers.size) { y -> numbers[y].digitToInt() }
        }
    }
    fun part1(input: List<String>): Int {
        return compute(parse(input))
    }
    fun part2(input: List<String>): Int {
        val init = parse(input)
        val size = init.size
        val extendedMap = Array(size * 5) { x ->
            Array(size * 5) { y ->
                val num = init[x % size][y % size] + (x / size + y / size)
                if (num > 9) num % 9 else num
            }
        }

        printWorld(extendedMap)
        return compute(extendedMap)
    }

    val input = readLines("Day15")
    val inputTest = readLines("Day15-Test")

    check(part1(inputTest).also { println(it) } == 40)
    check(part2(inputTest).also { println(it) } == 315)

    println(part1(input))
    println(part2(input))
}
