
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.sum
import com.github.leokash.adventofcode.utils.math.context.Context

private const val PART_ONE_EXPECTED = 1320
private const val PART_TWO_EXPECTED = 145

private data class Lens(val label: String, val focalLength: Int)

private fun Array<Lens?>.shift(from: Int) {
    var idx = from
    while (idx < size) {
        if (this[idx] == null) break
        this[idx - 1] = this[idx].also { this[idx++] = null }
    }
}
private fun Array<Lens?>.remove(key: String) {
    when (val idx = indexOfFirst { it?.label == key }) {
        -1 -> { /*NO-OP*/ }
        else -> { this[idx] = null; shift(idx + 1) }
    }
}
private fun Array<Lens?>.insert(key: String, length: Int) {
    when (val idx = indexOfFirst { it?.label == key }) {
        -1 -> { indexOfFirst { it == null }.let { if (it != -1) this[it] = Lens(key, length) } }
        else -> this[idx] = Lens(key, length)
    }
}

fun main() {
    fun hash(string: String): Int {
        return string.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
    }
    fun hashmap(string: String, boxes: MutableMap<Int, Array<Lens?>>) {
        when {
            '-' in string -> {
                val key = string.split('-')[0]
                boxes.getOrPut(hash(key)) { Array(9) { null } }.remove(key)
            }
            '=' in string -> {
                val (key, length) = string.split('=')
                boxes.getOrPut(hash(key)) { Array(9) { null } }.insert(key, length.toInt())
            }
        }
    }

    fun part1(input: String): Int {
        return input.split(',').sumOf { hash(it) }
    }
    fun part2(input: String): Int {
        return mutableMapOf<Int, Array<Lens?>>()
            .apply { input.split(',').onEach { hashmap(it, this) } }
            .entries.sum(Context<Int>()) { (box, lenses) ->
                lenses.foldIndexed(0) { i, acc, lens ->
                    acc + if (lens != null) (box + 1) * lens.focalLength * (i + 1) else 0
                }
            }
    }

    val input = readText("Day15")
    val inputTest = readText("Day15-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
