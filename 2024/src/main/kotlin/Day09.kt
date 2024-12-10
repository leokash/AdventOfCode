
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.sumIndexed
import com.github.leokash.adventofcode.utils.math.context.Context
import kotlin.math.min

private const val PART_ONE_EXPECTED = 1928L
private const val PART_TWO_EXPECTED = 2858L

sealed interface Block {
    val id: Int
}
data object FreeBlock: Block {
    override val id: Int = -1
    override fun toString(): String = "."
}
data class DataBlock(override val id: Int): Block {
    override fun toString(): String = "$id"
}

private fun String.makeFileSystem(): List<Block> {
    return buildList {
        var idx = -1
        for ((i, c) in this@makeFileSystem.withIndex()) {
            val size = c.digitToInt()
            idx = if (i % 2 == 0) idx + 1 else idx
            if (i % 2 == 0) repeat(size) { add(DataBlock(idx)) } else repeat(size) { add(FreeBlock) }
        }
    }
}

private fun List<Block>.defragFilesystem(movingFiles: Boolean): List<Block> {
    val blocks = mutableMapOf<Int, Int>()
    val spaces = mutableListOf<Pair<Int, Int>>()

    var idx = 0
    while (idx < size) {
        if (this[idx] is FreeBlock) {
            val size = (idx ..<size).takeWhile { this[it] is FreeBlock }.size
            spaces.add(idx to size)
            idx += size
            continue
        }

        if (this[idx] is DataBlock) {
            val id = this[idx].id
            val size = (idx..<size).takeWhile { this[it].id == id }.size
            blocks[idx] = size
            idx += size
            continue
        }

        idx++
    }

    return this
        .toMutableList()
        .apply {
            var blockIdx = lastIndex
            while (blockIdx > 0) {
                val blockSize = blocks[blockIdx]
                if (blockSize == null) {
                    blockIdx--
                    continue
                }

                for (i in spaces.indices) {
                    val (free, freeSize) = spaces[i]
                    if (free >= blockIdx) break
                    if (movingFiles && freeSize < blockSize) continue

                    val id = this[blockIdx].id
                    val offset = (blockIdx + blockSize) - 1
                    for (j in 0..<freeSize) {
                        if (this[offset - j].id != id) break
                        this[offset - j] = this[j + free].also { this[j + free] = this[offset - j] }
                    }

                    blocks.remove(blockIdx)
                    if (freeSize - blockSize <= 0) spaces.removeAt(i)
                    if (blockSize > freeSize) blocks[blockIdx] = blockSize - freeSize
                    if (freeSize > blockSize) spaces[i] = free + blockSize to freeSize - blockSize

                    blockIdx += min(blockSize, freeSize)
                    break
                }

                blockIdx--
            }
        }
}

fun main() {
    fun compute(input: String, movingFiles: Boolean = false): Long {
        return input
            .makeFileSystem()
            .defragFilesystem(movingFiles)
            .sumIndexed(Context()) { i, b -> 1L * i * if (b.id >= 0) b.id else 0 }
    }

    fun part1(input: String): Long = compute(input)
    fun part2(input: String): Long = compute(input, true)

    val input = readText("Day09")
    val inputTest = readText("Day09-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}
