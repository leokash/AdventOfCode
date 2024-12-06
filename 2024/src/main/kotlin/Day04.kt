
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.toCharMatrix
import com.github.leokash.adventofcode.utils.math.geometry.*
import com.github.leokash.adventofcode.utils.matrix.*

private const val PART_ONE_EXPECTED = 18
private const val PART_TWO_EXPECTED = 9

private fun CharMatrix.searchXmases(start: IntPoint): Boolean {
    return slice<Char>(start, 3, 3)?.let { slice ->
        slice[1,1] == 'A' && (
            slice[0, 0] == 'M' && slice[0, 2] == 'M' && slice[2,0] == 'S' && slice[2,2] == 'S' ||
            slice[0, 0] == 'S' && slice[0, 2] == 'S' && slice[2,0] == 'M' && slice[2,2] == 'M' ||
            slice[0, 0] == 'M' && slice[0, 2] == 'S' && slice[2,0] == 'M' && slice[2,2] == 'S' ||
            slice[0, 0] == 'S' && slice[0, 2] == 'M' && slice[2,0] == 'S' && slice[2,2] == 'M'
        )
    } ?: false
}

private fun CharMatrix.searchXmas(start: IntPoint, dir: Direction, cache: MutableSet<List<IntPoint>>): Boolean {
    val reversed = get(start) == 'S'
    fun validate(idx: Int, point: Point<Int>): Boolean {
        return getOrNull(point) == (if (reversed) "SAMX" else "XMAS")[idx]
    }

    var point: Point<Int>? = start
    for (i in 0..3) {
        if (point == null || point !in this || !validate(i, point))
            return false
        point = point.next(dir)
    }

    val line = point?.let { start.lineTo(it).dropLast(1) } ?: emptyList()
    return (line !in cache && line.reversed() !in cache).also {
        if (it) {
            cache.add(line)
            cache.add(line.reversed())
        }
    }
}

fun main() {
    fun compute(input: List<String>, usingSlice: Boolean = false): Int {
        val mat = input.toCharMatrix()
        val cache = mutableSetOf<List<IntPoint>>()
        return mat.foldIndexed(0) { x, y, acc, char ->
            acc + when(usingSlice) {
                true  -> if ((char == 'M' || char == 'S') && mat.searchXmases(Point(x, y))) 1 else 0
                false -> if (char == 'X' || char == 'S') Direction.all.count {  mat.searchXmas(Point(x, y), it, cache) } else 0
            }
        }
    }

    fun part1(input: List<String>): Int = compute(input)
    fun part2(input: List<String>): Int = compute(input, true)

    val input = readLines("Day04")
    val inputTest = readLines("Day04-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
