
@file:Suppress("unused")

package com.github.leokash.adventofcode.utils.matrix

import com.github.leokash.adventofcode.utils.int
import com.github.leokash.adventofcode.utils.geometry.points.ints.Point
import kotlin.math.min
import kotlin.math.max

fun <T, R> Matrix<T>.fold(initial: R, f: (R, T) -> R): R {
    return foldIndexed(initial) { _, _, acc, value ->
        f(acc, value)
    }
}
fun <T, R> Matrix<T>.foldIndexed(initial: R, f: (Int, Int, R, T) -> R): R {
    var acc = initial
    forEachIndexed { x, y, value ->
        acc = f(x, y, acc, value)
    }

    return acc
}

fun <T> Matrix<T>.count(predicate: (T) -> Boolean): Int {
    return fold(0) { acc, value -> acc + predicate(value).int }
}
fun <T> Matrix<T>.minBy(selector: (Int, Int, T) -> Int): Int {
    return foldIndexed(0) { x, y, acc, value -> min(acc, selector(x, y, value)) }
}
fun <T> Matrix<T>.maxBy(selector: (Int, Int, T) -> Int): Int {
    return foldIndexed(0) { x, y, acc, value -> max(acc, selector(x, y, value)) }
}
fun <T> Matrix<T>.countBy(predicate: (Int, Int, T) -> Boolean): Int {
    return foldIndexed(0) { x, y, acc, value -> acc + predicate(x, y, value).int }
}

fun <T> Matrix<T>.any(predicate: (Int, Int, T) -> Boolean): Boolean {
    for (x in rowIndices)
        for (y in columnIndices)
            if (predicate(x, y, this[x, y])) return true
    return false
}
fun <T> Matrix<T>.all(predicate: (Int, Int, T) -> Boolean): Boolean {
    for (x in rowIndices)
        for (y in columnIndices)
            if (!predicate(x, y, this[x, y])) return false
    return true
}
fun <T> Matrix<T>.filter(predicate: (Int, Int, T) -> Boolean): List<Pair<Point, T>> {
    return buildList {
        this@filter.forEachIndexed { x, y, value ->
            if (predicate(x, y, value))
                add(Point(x, y) to value)
        }
    }
}
