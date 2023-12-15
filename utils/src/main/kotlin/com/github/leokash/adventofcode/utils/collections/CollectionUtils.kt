
package com.github.leokash.adventofcode.utils.collections

import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.geometry.Point

fun <T> Collection<T>.product(selector: (T) -> Int): Int {
    return productIndexed { _, num -> selector(num) }
}
fun <T> Collection<T>.productIndexed(selector: (Int, T) -> Int): Int {
    return productIndexed(Context<Int>(), selector)
}

fun <T, R> Collection<T>.sum(ctx: Context<R>, selector: (T) -> R): R where R: Number, R: Comparable<R> {
    return sumIndexed(ctx) { _, obj -> selector(obj) }
}

fun <T, R> Collection<T>.sumIndexed(ctx: Context<R>, selector: (Int, T) -> R): R where R: Number, R: Comparable<R> {
    return foldIndexed(ctx.zero) { idx, acc, num -> ctx.add(acc, selector(idx, num)) }
}

fun <T, R> Collection<T>.product(ctx: Context<R>, selector: (T) -> R): R where R: Number, R: Comparable<R> {
    return productIndexed(ctx) { _, num -> selector(num) }
}
fun <T, R> Collection<T>.productIndexed(ctx: Context<R>, selector: (Int, T) -> R): R where R: Number, R: Comparable<R> {
    return foldIndexed(ctx.one) { idx, acc, num -> ctx.mul(acc, selector(idx, num)) }
}

fun <T> Collection<T>.chunkedBy(predicate: (T) -> Boolean): List<List<T>> {
    return chunkedByIndexed { _, value -> predicate(value) }
}
fun <T> Collection<T>.chunkedByIndexed(predicate: (Int, T) -> Boolean): List<List<T>> {
    return buildList {
        var container: MutableList<T>? = null
        this@chunkedByIndexed.forEachIndexed { idx, value ->
            container = if (predicate(idx, value)) {
                mutableListOf<T>().also { add(it) }
            } else {
                (container ?: mutableListOf<T>().also { add(it) }).also { it.add(value) }
            }
        }
    }
}
