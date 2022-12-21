import com.github.leokash.adventofcode.utils.readText

private val Long.decremented: Long get() {
    return if (this == 0L) 6 else this - 1
}

fun main() {
    fun part1(input: List<Long>, days: Int): Long {
        return (1..days).fold(input) { list, _ ->
            buildList {
                val zeros = list.count { it == 0L }
                for (num in list) {
                    add(num.decremented)
                }

                repeat(zeros) { add(8) }
            }
        }.size.toLong()
    }
    fun part2(input: List<Long>, days: Int): Long {
        return (1..days).fold(input.groupBy { it }.mapValues { it.value.size.toLong() }) { map, _ ->
            buildMap {
                val zeros = map[0]
                for ((num, count) in map) {
                    val idx = num.decremented
                    put(idx, count + getOrDefault(idx, 0))
                }

                zeros?.let { put(8, it) }
            }
        }.values.sumOf { it }
    }

    val input = readText("Day06").split(',').map { it.trim().toLong() }
    val inputTest = listOf(3L, 4, 3, 1, 2)

    check(part1(inputTest, 18) == 26L)
    check(part1(inputTest, 80) == 5934L)
    check(part2(inputTest, 256) == 26984457539)

    println(part1(input, 80))
    println(part2(input, 256))
}
