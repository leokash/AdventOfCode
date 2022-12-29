
package com.github.leokash.adventofcode.utils.collections

import com.github.leokash.adventofcode.utils.CircularCounter

class CircularList<out T>(start: Int = 0, private val list: List<T>): List<T> by list {
    val index: Int get() {return counter.get() }
    private val counter = CircularCounter(start = start, max = list.lastIndex)

    fun next(): T {
        return list[counter.getAndIncrement()]
    }
}
