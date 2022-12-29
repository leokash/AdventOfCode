
@file:Suppress("unused")

package com.github.leokash.adventofcode.utils

data class CircularCounter(private val min: Int = 0, private val max: Int, private val start: Int = 0) {
    private var idx: Int = 0

    init { set(start) }

    private fun increment() {
        idx = (idx + 1) % (max + 1)
    }
    private fun decrement() {
        idx = (max - -idx) % (max + 1)
    }

    fun get(): Int {
        return idx
    }
    fun set(num: Int) {
        idx = when {
            num < min -> ((max - -num) + 1) % (max + 1)
            num > max -> (num - 1) % max
            else -> num
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
}

fun main() {
    val c = CircularCounter(0, 6, -1)
    repeat(5) {
        println("idx: ${c.getAndIncrement()}")
    }
}