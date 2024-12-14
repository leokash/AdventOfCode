
@file:Suppress("unused")

package com.github.leokash.adventofcode.utils

typealias Fun1<T, R> = (T) -> R
typealias Fun2<T1, T2, R> = (T1, T2) -> R
typealias Fun3<T1, T2, T3, R> = (T1, T2, T3) -> R
typealias Fun4<T1, T2, T3, T4, R> = (T1, T2, T3, T4) -> R
typealias Fun5<T1, T2, T3, T4, T5, R> = (T1, T2, T3, T4, T5) -> R

private class State4<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)
private class State5<T1, T2, T3, T4, T5>(val t1: T1, val t2: T2, val t3: T3, val t4: T4, val t5: T5)

fun <T, R> Fun1<T, R>.memoized(): Fun1<T, R> {
    val cache = mutableMapOf<T, R>()
    return { v ->
        cache.getOrPut(v) { this@memoized(v) }
    }
}

fun <T1, T2, R> Fun2<T1, T2, R>.memoized(): Fun2<T1, T2, R> {
    val cache = mutableMapOf<Pair<T1, T2>, R>()
    return { v1, v2 ->
        cache.getOrPut(v1 to v2) { this@memoized(v1, v2) }
    }
}

fun <T1, T2, T3, R> Fun3<T1, T2, T3, R>.memoized(): Fun3<T1, T2, T3, R> {
    val cache = mutableMapOf<Triple<T1, T2, T3>, R>()
    return { v1, v2, v3 ->
        cache.getOrPut(Triple(v1, v2, v3)) { this@memoized(v1, v2, v3) }
    }
}

fun <T1, T2, T3, T4, R> Fun4<T1, T2, T3, T4, R>.memoized(): Fun4<T1, T2, T3, T4, R> {
    val cache = mutableMapOf<State4<T1, T2, T3, T4>, R>()
    return { v1, v2, v3, v4 ->
        cache.getOrPut(State4(v1, v2, v3, v4)) { this@memoized(v1, v2, v3, v4) }
    }
}

fun <T1, T2, T3, T4, T5, R> Fun5<T1, T2, T3, T4, T5, R>.memoized(): Fun5<T1, T2, T3, T4, T5, R> {
    val cache = mutableMapOf<State5<T1, T2, T3, T4, T5>, R>()
    return { v1, v2, v3, v4, v5 ->
        cache.getOrPut(State5(v1, v2, v3, v4, v5)) { this@memoized(v1, v2, v3, v4, v5) }
    }
}
