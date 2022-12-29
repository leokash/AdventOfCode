
package com.github.leokash.adventofcode.utils.collections

operator fun <E> List<E>.component6(): E = this[5]
operator fun <E> List<E>.component7(): E = this[6]
operator fun <E> List<E>.component8(): E = this[7]
operator fun <E> List<E>.component9(): E = this[8]
operator fun <E> List<E>.component10(): E = this[9]

fun <T> Collection<T>.product(selector: (T) -> Int): Int {
    return productIndexed { _, num -> selector(num) }
}
fun <T> Collection<T>.productIndexed(selector: (Int, T) -> Int): Int {
    return foldIndexed(1) { idx, acc, num -> acc * selector(idx, num) }
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
