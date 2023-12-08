
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.indexedSumOf
import com.github.leokash.adventofcode.utils.math.context.Context

private const val PART_ONE_EXPECTED = 6440
private const val PART_TWO_EXPECTED = 5905

private val cardToAlphabet = mapOf('A' to 'n', 'K' to 'm', 'Q' to 'l', 'J' to 'k', 'T' to 'j') + (2..9).associate { it.digitToChar() to (96 + it).toChar() }

private data class Hand(
    val bid: Int,
    private val cards: String,
    private val boost: Boolean
) {
    enum class Type {
        High, OnePair, TwoPairs, ThreeOfAKind, FullHouse, FourOfAKind, FiveOfAKind,
    }

    val score: Type get () = when (groups.size) {
        1 -> Type.FiveOfAKind
        in 2..4 -> when (groups[0].size) {
            4 -> Type.FourOfAKind
            3 -> if (groups[1].size == 2) Type.FullHouse else Type.ThreeOfAKind
            else -> if (groups[1].size == 2) Type.TwoPairs else Type.OnePair
        }

        else -> Type.High
    }
    val flatten: String get() = cards
        .map { if (boost && it == 'J') 'a' else cardToAlphabet.getValue(it) }
        .joinToString("")

    private val groups: List<List<Char>> get() = cards
        .groupBy { it }
        .values.sortedByDescending { it.size }
        .let { groups ->
            if (groups.size == 1 || !boost) groups else groups
                .filter { it[0] != 'J' }
                .mapIndexed { idx, list -> if (idx == 0) list.inflate(cards.count { it == 'J' }) else list }
        }

    private fun List<Char>.inflate(count: Int): List<Char> {
        return this.toMutableList().apply {
            repeat(count) {
                add(this[0])
            }
        }
    }

    companion object {
        fun from(string: String, allowJoker: Boolean): Hand {
            return string
                .trim()
                .split(" ")
                .let { Hand(it[1].toInt(), it[0], allowJoker) }
        }
    }
}

fun main() {
    fun compute(input: List<String>, firstPart: Boolean): Int {
        return input
            .map { Hand.from(it, !firstPart) }
            .sortedWith(compareBy({ it.score }, { it.flatten }))
            .indexedSumOf(Context<Int>()) { idx, hand -> (idx + 1) * hand.bid }
    }

    fun part1(input: List<String>): Int = compute(input, true)
    fun part2(input: List<String>): Int = compute(input, false)

    val input = readLines("Day07")
    val inputTest = readLines("Day07-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
