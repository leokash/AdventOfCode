
package com.github.leokash.adventofcode.utils.graphs

typealias Mode<T> = PathFinding.Mode<T>
private typealias BFS<T> = PathFinding.Mode.BFS<T>

class Graph<T> {
    private val map = mutableMapOf<Node<T>, MutableList<Edge<T>>>()

    fun add(data: T): Node<T> {
        return Node(data).also {
            map[it] = mutableListOf()
        }
    }
    fun find(data: T): Node<T>? {
        return find { it == data }
    }
    fun find(predicate: (T) -> Boolean): Node<T>? {
        return map.keys.firstOrNull { predicate(it.value) }
    }
    fun addEdge(source: Node<T>, destination: Node<T>, weight: Double? = 0.0) {
        map[source]?.add(Edge(source, destination, weight))
    }
    fun neighbors(node: Node<T>, predicate: (T) -> Boolean = { true }): List<Node<T>> {
        return map[node]?.mapNotNull { if (predicate(it.destination.value)) it.destination else null } ?: emptyList()
    }
    fun findShortestPath(start: Node<T>, finish: Node<T>, mode: Mode<Node<T>> = BFS { neighbors(it) }): PathFinding.Result<Node<T>>? {
        return PathFinding.findShortestPath(start, finish, mode)
    }

    override fun toString(): String {
        return buildString {
            for ((n, edges) in map) {
                append("${n.value} -> ")
                append(edges.joinToString(prefix = "[", postfix = "]") { "${it.destination.value}" })
                append("\n")
            }
        }
    }
}
