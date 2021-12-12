
data class Counter(var data: Int)
private val String.allLowercase: Boolean get() {
    return all { it.isLowerCase() }
}
private fun excludeCave(cave: String, allowExtraCaves: Boolean, visited: List<String>): Boolean {
    return cave == "start" ||
            (cave.allLowercase && cave in visited && if (allowExtraCaves) {
                visited
                    .filter { it.allLowercase }
                    .groupBy { it }.values.any { it.size >= 2 } } else true)
}

private fun compute(
    cave: String,
    counter: Counter,
    allowExtraCaves: Boolean,
    visited: MutableList<String>,
    world: Map<String, List<String>>)
{
    visited.add(cave)
    when (cave) {
        "end" -> { counter.data++ }
        else  -> {
            world[cave]
                ?.filterNot { excludeCave(it, allowExtraCaves, visited) }
                ?.forEach { dest ->
                    compute(
                        dest,
                        counter,
                        allowExtraCaves,
                        mutableListOf<String>().also { it.addAll(visited) },
                        world
                    )
                }
        }
    }
}

fun main() {
    fun parse(input: List<String>): Map<String, List<String>> {
        return buildMap {
            for (string in input) {
                val (left, right) = string.trim().split('-')
                this[left] = (this[left] ?: listOf()) + right
                this[right] = (this[right] ?: listOf()) + left
            }
        }
    }

    fun part1(input: List<String>): Int {
        val counter = Counter(0)
        compute("start", counter, false, mutableListOf(), parse(input))
        return counter.data
    }
    fun part2(input: List<String>): Int {
        val counter = Counter(0)
        compute("start", counter, true, mutableListOf(), parse(input))
        return counter.data
    }

    val input = readLines("Day12")
    val inputTest1 = readLines("Day12-Test1")
    val inputTest2 = readLines("Day12-Test2")
    val inputTest3 = readLines("Day12-Test3")

    check(part1(inputTest1) == 10)
    check(part1(inputTest2) == 19)
    check(part1(inputTest3) == 226)

    check(part2(inputTest1) == 36)
    check(part2(inputTest2) == 103)
    check(part2(inputTest3) == 3509)

    println(part1(input))
    println(part2(input))
}
