

private fun Array<Array<Int>>.basin(x: Int, y: Int, visited: MutableList<Point>): Int {
    visited.add(Point(x, y))
    val predicate = { i: Int, j: Int, num: Int ->
        num < 9 && visited.find { (px, py) -> px == i && py == j } == null
    }
    return neighbors(x, y, predicate)
        .onEach { (point, _) -> visited.add(point) }
        .fold(0) { sum, (point, _) -> sum + 1 + basin(point.x, point.y, visited) }
}
private fun <T, R> Array<Array<T>>.foldIndexed(initial: R, transform: (Int, Int, R, T) -> R): R {
    var result = initial
    for (x in indices) {
        for (y in this[x].indices) {
            result = transform(x, y, result, this[x][y])
        }
    }

    return result
}
private fun <T> Array<Array<T>>.neighbors(x: Int, y: Int, predicate: (Int, Int, T) -> Boolean = {_,_,_ -> true }): List<Pair<Point, T>> {
    return buildList {
        val arr = this@neighbors
        val xMax = arr.size
        val yMax = arr[0].size
        (x - 1).let { if (it >= 0 && predicate(it, y, arr[it][y])) add(Point(it, y) to arr[it][y]) }
        (y - 1).let { if (it >= 0 && predicate(x, it, arr[x][it])) add(Point(x, it) to arr[x][it]) }
        (x + 1).let { if (it < xMax && predicate(it, y, arr[it][y])) add(Point(it, y) to arr[it][y]) }
        (y + 1).let { if (it < yMax && predicate(x, it, arr[x][it])) add(Point(x, it) to arr[x][it]) }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = Array(input.size) { x ->
            val row = input[x].trim().map { it.digitToInt() }
            Array(row.size) { y -> row[y] }
        }
        return grid.foldIndexed(0) { x, y, sum, num ->
            sum + grid.neighbors(x, y).let { neighbors ->
                if (neighbors.all { it.second > num }) num + 1 else 0
            }
        }
    }
    fun part2(input: List<String>): Int {
        val grid = Array(input.size) { x ->
            val row = input[x].trim().map { it.digitToInt() }
            Array(row.size) { y -> row[y] }
        }

        return grid.foldIndexed(mutableListOf<Int>()) { x, y, arr, num ->
            arr.also { it.add(grid.neighbors(x, y).let { neighbors ->
                if (neighbors.all { (_, n) -> n > num }) grid.basin(x, y, mutableListOf()) + 1 else 0
            }) }
        }.sorted().takeLast(3).reduce { acc, i -> acc * i }
    }

    val input = readLines("Day09")
    val inputTest = readLines("Day09-Test")

    check(part1(inputTest) == 15)
    check(part2(inputTest) == 1134)

    println(part1(input))
    println(part2(input))
}
