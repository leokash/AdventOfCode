
package com.github.leokash.adventofcode.utils.collections

import com.github.leokash.adventofcode.utils.math.context.Context

fun <T, R> Iterable<T>.sumOf(ctx: Context<R>, selector: (T) -> R): R where R: Number, R: Comparable<R> {
    return indexedSumOf(ctx) { _, obj -> selector(obj) }
}

fun <T, R> Iterable<T>.indexedSumOf(ctx: Context<R>, selector: (Int, T) -> R): R where R: Number, R: Comparable<R> {
    var idx = 0
    var sum = ctx.zero
    for (obj in this)
        sum = ctx.add(sum, selector(idx++, obj))

    return sum
}
