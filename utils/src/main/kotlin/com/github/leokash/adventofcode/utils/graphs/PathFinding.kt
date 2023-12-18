
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

    data class VisitedNode<T>(val score: Int, val previous: T? = null)
    private data class ScoredNode<T>(val element: T, val score: Int, val heuristic: Int) {
        val computedScore: Int get() { return score + heuristic }
    }

    class Result<T>(val start: T, private val finish: T, private val result: Map<T, VisitedNode<T>>) {
        fun getScore(node: T = finish) = result[node]?.score ?: error("Result for $node not found")

        tailrec fun getPath(node: T = finish, path: List<T> = emptyList()): List<T> {
            val previous = result[node]?.previous
            return if (previous == null) listOf(node) + path else getPath(previous, listOf(node) + path)
        }
    }

    companion object {
        fun <T> findShortestPath(start: T, finish: T, mode: Mode<T>): Result<T>? {
            return findShortestPath(start, mode) { it == finish }
        }
        fun <T> findShortestPath(start: T, mode: Mode<T>, predicate: (T) -> Boolean): Result<T>? {
            var finish: T? = null
            val queue = PriorityQueue<ScoredNode<T>>(compareBy { n -> n.computedScore })
            val visited = mutableMapOf<T, VisitedNode<T>>(start to VisitedNode(0, null))

            queue.add(ScoredNode(start, 0, 0))

            while (queue.isNotEmpty()) {
                val tmp = queue.remove()
                if (tmp == null || predicate(tmp.element)) {
                    tmp?.also { n -> finish = n.element }
                    break
                }

                val (node, score) = tmp
                val neighbors = mode.neighbors(node)
                    .filter { it !in visited }
                    .map { next -> ScoredNode(next, score + mode.cost(node, next), mode.heuristic(next)) }

                queue.addAll(neighbors)
                visited.putAll(neighbors.associate { it.element to VisitedNode(it.score, node) })
            }

            return finish?.let { f -> Result(start, f, visited) }
        }
    }
}
