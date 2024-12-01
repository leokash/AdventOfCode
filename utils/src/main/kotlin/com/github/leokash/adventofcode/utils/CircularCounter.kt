
@file:Suppress("unused")

package com.github.leokash.adventofcode.utils

import kotlin.math.abs

data class CircularCounter(private val min: Int = 0, private val max: Int, private val start: Int = 0) {
    private var idx: Int = 0

    init { set(start) }

    private fun increment() {
        set(idx + 1)
    }
    private fun decrement() {
        set(idx - 1)
    }

    fun get(): Int {
        return idx
    }
    fun set(num: Int) {
        idx = when {
            num in min..max -> num
            num > max -> num % (max + 1)
            else -> ((max - -(if (abs(num) >= max) num % (max + 1) else num)) + 1) % (max + 1)
        }
    }
    fun getAndIncrement(): Int {
        return idx.also { increment() }
    }
    fun getAndDecrement(): Int {
        return idx.also { decrement() }
    }
    fun incrementAndGet(): Int {
        return increment().let { idx }
    }
    fun decrementAndGet(): Int {
        return decrement().let { idx }
    }

    fun repeatDecrement(times: Int): CircularCounter {
        repeat(times) {
            decrement()
        }

        return this
    }

    fun repeatIncrement(times: Int): CircularCounter {
        repeat(times) {
            increment()
        }

        return this
    }
}
