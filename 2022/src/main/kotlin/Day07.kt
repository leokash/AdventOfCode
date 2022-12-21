
import com.github.leokash.adventofcode.utils.readLines

private const val PART_ONE_SIZE = 100_000
private const val PART_ONE_EXPECTED = 95437
private const val PART_TWO_EXPECTED = 24_933_642
private const val FILESYSTEM_TOTAL_SIZE = 70_000_000
private const val FILESYSTEM_NEEDED_SPACE = 30_000_000

fun main() {
    fun part1(node: Node): Int {
        return node
            .filter { it is Dir && it.size <= PART_ONE_SIZE }
            .sumOf { it.size }
    }
    fun part2(node: Node): Int {
        val used = FILESYSTEM_TOTAL_SIZE - node.size
        return node
            .filter { it is Dir && (used + it.size) >= FILESYSTEM_NEEDED_SPACE }
            .minOf { it.size }
    }

    val node = readLines("Day07").toNode()
    val testNode = readLines("Day07-Test").toNode()

    check(part1(testNode) == PART_ONE_EXPECTED)
    println(part1(node))

    check(part2(testNode) == PART_TWO_EXPECTED)
    println(part2(node))
}

private sealed class Node {
    abstract val name: String
    abstract val size: Int
}

private class Dir(override val name: String, val parent: Dir?): Node() {
    val children: MutableList<Node> = mutableListOf()
    override val size: Int get() = children.fold(0) { acc, node -> acc + node.size }
}
private class File(override val name: String, override val size: Int): Node()

private fun Node.filter(predicate: (Node) -> Boolean): List<Node> {
    fun filter(node: Node, accumulator: MutableList<Node>, predicate: (Node) -> Boolean) {
        if (predicate(node)) accumulator.add(node)
        (node as? Dir)?.children?.forEach { filter(it, accumulator, predicate) }
    }

    return buildList { filter(this@filter, this, predicate) }
}

private val rgx = Regex("\\$ cd [a-z]+")

private fun List<String>.toNode(): Node = Dir("/", null).also { root ->
    this.drop(1).fold(root) { cur, cmd ->
        when {
            cmd[0] == 'd' -> cur.also { it.children.add(Dir(cmd.split(" ").last(), it)) }
            cmd == "$ cd .." -> cur.parent ?: error("$cur expected to have a parent, but none present")
            cmd[0].isDigit() -> cur.also { it.children.add(cmd.split(" ").let { (s, name) -> File(name, s.toInt()) }) }
            cmd.matches(rgx) -> cmd.split(" ")[2].let { d -> cur.children.first { d == it.name } } as? Dir ?: error("!!")

            else -> cur
        }
    }
}
