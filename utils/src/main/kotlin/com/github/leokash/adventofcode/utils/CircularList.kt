
package com.github.leokash.adventofcode.utils

class CircularList<out T>(start: Int = 0, private val list: List<T>): List<T> by list {
    private val counter = CircularCounter(start = start, max = list.lastIndex)

    fun next(): T {
        return list[counter.getAndIncrement()]
    }
    fun nextWithIndex(): Pair<Int, T> {
        return counter.get() to list[counter.getAndIncrement()]
    }
    fun <R> nextMappedWithIndex(transform: (T) -> R): Pair<Int, R> {
        return counter.get() to transform(list[counter.getAndIncrement()])
    }
}
