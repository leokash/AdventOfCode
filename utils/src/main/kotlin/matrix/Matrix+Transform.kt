
package matrix

import Point

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
    return fold(0) { acc, value -> if (predicate(value)) acc + 1 else acc }
}
fun <T> Matrix<T>.filter(predicate: (Int, Int, T) -> Boolean): List<Pair<Point, T>> {
    return foldIndexed(mutableListOf()) { x, y, list, value ->
        list.also {
            if (predicate(x, y, value))
                it.add(Point(x, y) to value)
        }
    }
}
