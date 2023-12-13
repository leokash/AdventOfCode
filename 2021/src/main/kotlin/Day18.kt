
import com.github.leokash.adventofcode.utils.collections.permutations
import com.github.leokash.adventofcode.utils.readLines

private sealed interface SnailfishNumber {
    fun magnitude(): Int

    fun reduce() {
        while(true)
            if (!explode() && !split()) break
    }
    fun findParent(node: SnailfishNumber): Grouping? {
        return when (this) {
            is Single -> null
            is Grouping -> {
                if (left === node) return this
                if (right === node) return this
                return left.findParent(node) ?: right.findParent(node)
            }
        }
    }
    companion object {
        fun from(input: String): SnailfishNumber {
            val stack = mutableListOf<SnailfishNumber>()
            input.forEach { c ->
                when (c) {
                    in '0'..'9' -> stack.add(Single(c.digitToInt()))
                    ']' -> {
                        val rhs = stack.removeLast()
                        val lhs = stack.removeLast()
                        stack.add(Grouping(lhs, rhs))
                    }
                }
            }

            return stack.removeFirst()
        }
    }
}
private data class Single(var value: Int): SnailfishNumber {
    override fun magnitude(): Int = value
    override fun toString(): String {
        return "$value"
    }
}
private data class Grouping(var left: SnailfishNumber, var right: SnailfishNumber): SnailfishNumber {
    override fun magnitude(): Int {
        return 3 * left.magnitude() + 2 * right.magnitude()
    }

    override fun toString(): String {
        return "[$left,$right]"
    }
}

private fun SnailfishNumber.split(): Boolean {
    fun find(number: SnailfishNumber, parent: Grouping?): Pair<Single, Grouping?>? {
        return when (number) {
            is Single -> if (number.value >= 10) number to parent else null
            is Grouping -> {
                find(number.left, number) ?: find(number.right, number)
            }
        }
    }

    val (number, parent) = find(this, null) ?: return false
    if (number.value >= 10) {
        val lhs = number.value / 2
        val rhs = (number.value / 2).let { if (number.value % 2 == 0) it else it + 1 }
        parent?.let {
            if (it.left === number) {
                it.left = Grouping(Single(lhs), Single(rhs))
            } else {
                it.right = Grouping(Single(lhs), Single(rhs))
            }
        }
    }

    return true
}
private fun SnailfishNumber.explode(): Boolean {
    fun SnailfishNumber.orderedSingles(): List<Single> {
        return when (this) {
            is Single -> listOf(this)
            is Grouping -> left.orderedSingles() + right.orderedSingles()
        }
    }
    fun SnailfishNumber.groupingsWithDepthList(depth: Int = 0): List<Pair<Int, Grouping>> {
        return when (this) {
            is Single -> emptyList()
            is Grouping -> left.groupingsWithDepthList(depth + 1) + listOf(depth to this) + right.groupingsWithDepthList(depth + 1)
        }
    }

    val singles = orderedSingles()
    val explode = groupingsWithDepthList().firstOrNull { (depth, _ ) -> depth == 4 }?.second ?: return false
    singles.elementAtOrNull(singles.indexOfFirst { it === explode.left } - 1)?.let { it.value += (explode.left as Single).value }
    singles.elementAtOrNull(singles.indexOfFirst { it === explode.right } + 1)?.let { it.value += (explode.right as Single).value }

    val parent = findParent(explode) ?: return false
    if (parent.left == explode) parent.left = Single(0) else parent.right = Single(0)

    return true
}
private operator fun SnailfishNumber.plus(other: SnailfishNumber): SnailfishNumber {
    return Grouping(this, other).also { it.reduce() }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { SnailfishNumber.from(it) }
            .reduce { acc, node -> acc + node }
            .magnitude()
    }
    fun part2(input: List<String>): Int {
        return input
            .permutations()
            .windowed(2)
            .maxOf { (lhs, rhs) -> (SnailfishNumber.from(lhs) + SnailfishNumber.from(rhs)).magnitude() }
    }

    val input = readLines("Day18")
    val inputTest = readLines("Day18-Test")

    check(part1(inputTest) == 4140)
    check(part2(inputTest) == 3993)

    println(part1(input))
    println(part2(input))
}
