
private const val WIN = 6
private const val DRAW = 3
private const val ROCK_SCORE = 1
private const val PAPER_SCORE = 2
private const val SCISSORS_SCORE = 3
private const val PART_ONE_EXPECTED = 15
private const val PART_TWO_EXPECTED = 12

fun main() {
    fun computeGame(input: List<String>, freePlay: Boolean = true): Int = input
        .map { it.trim().split(" ") }
        .sumOf { (opp, me) -> computeScore(translate(me, opp, freePlay), opp) }

    fun part1(input: List<String>): Int = computeGame(input)
    fun part2(input: List<String>): Int = computeGame(input, false)


    val input = readLines("Day02")
    val inputTest = readLines("Day02-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}

private val wins = mapOf("A" to "B", "B" to "C", "C" to "A")
private val loses = mapOf("A" to "C", "B" to "A", "C" to "B")

private fun computeScore(player1: String, player2: String): Int = when (player1) {
    "A" -> ROCK_SCORE + when(player2) { "A" -> DRAW; "C" -> WIN; else -> 0 }
    "B" -> PAPER_SCORE + when(player2) { "A" -> WIN; "B" -> DRAW; else -> 0 }
    "C" -> SCISSORS_SCORE + when(player2) { "B" -> WIN; "C" -> DRAW; else -> 0 }
    else -> error("!!")
}

private fun translate(player1: String, player2: String, freePlay: Boolean): String = when (player1) {
    "Y" -> if (freePlay) "B" else player2
    "X" -> if (freePlay) "A" else loses[player2] ?: error("!!")
    "Z" -> if (freePlay) "C" else wins[player2] ?: error("!!")
    else -> error("!!")
}
