
@file:Suppress("unused", "LoopWithTooManyJumpStatements")

package com.github.leokash.adventofcode.utils.graphs

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
    private data class ScoredNode<T>(val element: T, val score: Int, val heuristic: Int): Comparable<ScoredNode<T>> {
        val computedScore: Int get() { return score + heuristic }

        override fun compareTo(other: ScoredNode<T>): Int {
            return computedScore.compareTo(other.computedScore)
        }
    }

    class Result<T>(val start: T, private val finish: T, private val result: Map<T, VisitedNode<T>>) {
        fun getScore(node: T = finish) = result[node]?.score ?: error("Result for $node not found")

        tailrec fun getPath(node: T = finish, path: List<T> = emptyList()): List<T> {
            val previous = result[node]?.previous
            return if (previous == null) listOf(node) + path else getPath(previous, listOf(node) + path)
        }
    }

    companion object {
        private class Queue<T: Comparable<T>>(elements: Iterable<T>) {
            constructor(element: T): this(listOf(element))

            val isEmpty: Boolean get() {
                return store.size == 0
            }
            private val store = mutableListOf<T>()

            init {
                store.addAll(elements)
            }

            fun pop(): T? {
                return when (store.size) {
                    0 -> null
                    1 -> store.removeAt(0)
                    else -> (store.sort()).let { store.removeAt(0) }
                }
            }
            fun push(element: T) {
                store += element
            }
            fun pushAll(elements: Iterable<T>) {
                store.addAll(elements)
            }
        }

        fun <T> findShortestPath(start: T, finish: T, mode: Mode<T>): Result<T>? {
            return findShortestPath(start, mode) { it == finish }
        }
        fun <T> findShortestPath(start: T, mode: Mode<T>, predicate: (T) -> Boolean): Result<T>? {
            var finish: T? = null
            val queue = Queue(ScoredNode(start, 0, 0))
            val visited = mutableMapOf<T, VisitedNode<T>>(start to VisitedNode(0, null))

            while (!queue.isEmpty) {
                val tmp = queue.pop()
                if (tmp == null || predicate(tmp.element)) {
                    tmp?.also { n -> finish = n.element }
                    break
                }

                val (node, score) = tmp
                val neighbors = mode.neighbors(node)
                    .filter { it !in visited }
                    .map { next -> ScoredNode(next, score + mode.cost(node, next), mode.heuristic(next)) }

                queue.pushAll(neighbors)
                visited.putAll(neighbors.associate { it.element to VisitedNode(it.score, node) })
            }

            return finish?.let { f -> Result(start, f, visited) }
        }
    }
}
