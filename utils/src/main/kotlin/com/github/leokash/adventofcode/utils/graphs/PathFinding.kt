
@file:Suppress("unused", "LoopWithTooManyJumpStatements")

package com.github.leokash.adventofcode.utils.graphs

import java.util.PriorityQueue

/*
https://en.wikipedia.org/wiki/A*_search_algorithm
https://en.wikipedia.org/wiki/Breadth-first_search
https://www.redblobgames.com/pathfinding/a-star/introduction.html
*/

class PathFinding {
    sealed class Mode<T> {
        abstract val cost: (lhs: T, rhs: T) -> Int
        abstract val heuristic: (node: T) -> Int
        abstract val neighbors: (node: T) -> List<T>

        data class BFS<T>(
            override val neighbors: (node: T) -> List<T>
        ): Mode<T>() {
            override val cost: (lhs: T, rhs: T) -> Int get() = { _, _ -> 1 }
            override val heuristic: (node: T) -> Int get() = { 0 }
        }

        data class GREEDY<T>(
            override val cost: (lhs: T, rhs: T) -> Int,
            override val neighbors: (node: T) -> List<T>,
        ): Mode<T>() {
            override val heuristic: (node: T) -> Int get() = { 0 }
        }

        data class ASTAR<T>(
            override val cost: (lhs: T, rhs: T) -> Int,
            override val neighbors: (node: T) -> List<T>,
            override val heuristic: (node: T) -> Int,
        ): Mode<T>()
    }

    data class Result<T>(val cost: Int, val path: List<T>)

    companion object {
        fun <T> findShortestPath(start: T, finish: T, mode: Mode<T>): Result<T>? {
            return findPaths(start, true, mode) { it == finish }.firstOrNull()
        }

        fun <T> findShortestPath(start: T, mode: Mode<T>, predicate: (T) -> Boolean): Result<T>? {
            return findPaths(start, true, mode, predicate = predicate).firstOrNull()
        }

        fun <T> findPaths(start: T, finish: T, mode: Mode<T>, maxCost: Int = Int.MAX_VALUE): List<Result<T>> {
            return findPaths(start, false, mode, maxCost) { it == finish }
        }

        fun <T> findPaths(start: T, mode: Mode<T>, maxCost: Int = Int.MAX_VALUE, predicate: (T) -> Boolean): List<Result<T>> {
            return findPaths(start, false, mode, maxCost, predicate)
        }

        private fun <T> findPaths(start: T, firstOnly: Boolean, mode: Mode<T>, maxCost: Int = Int.MAX_VALUE, predicate: (T) -> Boolean): List<Result<T>> {
            return buildList {
                val seen = mutableSetOf<T>()
                val queue = PriorityQueue<Triple<T, Int, List<T>>>(compareBy { it.second }).apply {
                    add(Triple(start, 0, emptyList()))
                }

                while (queue.isNotEmpty()) {
                    val (obj, cost, path) = queue.remove() ?: break

                    seen += obj
                    if (predicate(obj) && cost <= maxCost) {
                        add(Result(cost, path + obj))
                        if (firstOnly) break
                    }

                    queue += mode.neighbors(obj)
                        .filter { it !in seen }
                        .map { Triple(it, cost + mode.cost(obj, it) + mode.heuristic(it), path + obj) }
                }
            }
        }
    }
}
