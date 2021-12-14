
package matrix

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
