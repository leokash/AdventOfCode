
@file:Suppress("all")

import graphs.PathFinding
import graphs.PathFinding.Mode.BFS as BFS
import kotlin.math.max

private const val PART_ONE_EXPECTED = 1651
private const val PART_TWO_EXPECTED = 1707

private data class Valve(val name: String, val rate: Int, val links: List<String>)
private data class State(val time: Int, val current: String, val visited: Set<String>, val allowNextPlayer: Boolean)

fun main() {
    Logger.debug = false
    fun compute(start: String, time: Int, players: Int, input: List<String>): Int {
        val cache = mutableMapOf<State, Int>()
        val valves = parse(input)
        val possibles = valves.filter { (_, v) -> v.rate > 0 }
        val distances = valves.values.associate { v ->
            v.name to valves.keys.filter { it != v.name }.map { n ->
                n to (PathFinding.findShortestPath(v.name, n, BFS { valves.getValue(it).links })?.getScore() ?: 0)
            }
        }

        fun findBestScore(t: Int, cur: String, visited: Set<String>, allowSecondPlayer: Boolean): Int {
            val state = State(t, cur, visited, allowSecondPlayer)
            return cache.getOrPut(state) {
                val max = distances.getValue(cur).filter { it.first !in visited }.maxOf { (next, dist) ->
                    val nt = t - dist - 1
                    val nv = possibles[next]
                    if (nv == null || nt <= 0) 0 else {
                        nv.rate * nt + findBestScore(nt, next, visited + next, allowSecondPlayer)
                    }
                }

                if (allowSecondPlayer) max(max, findBestScore(time, start, visited, false)) else max
            }
        }

        return findBestScore(time, start, emptySet(), players > 1)
    }

    fun part1(input: List<String>): Int = compute("AA", 30, 1, input)
    fun part2(input: List<String>): Int = compute("AA", 26, 2, input)

    val input = readLines("Day16")
    val inputTest = readLines("Day16-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(inputTest) == PART_TWO_EXPECTED)
    println(part2(input))
}

private fun parse(input: List<String>): Map<String, Valve> {
    return input
        .map { string ->
            Valve(
                string.substringAfter("Valve ").take(2),
                string.substringAfter("rate=").takeWhile { it != ';' }.toInt(),
                string.substringAfter("lead").dropWhile { it !in 'A'..'Z' }.split(", ")
            )
        }
        .associateBy { it.name }
}
