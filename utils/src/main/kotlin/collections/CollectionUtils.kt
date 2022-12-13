package collections

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

fun <T> Collection<T>.product(selector: (T) -> Int): Int {
    return productIndexed { _, num -> selector(num) }
}
fun <T> Collection<T>.productIndexed(selector: (Int, T) -> Int): Int {
    return foldIndexed(1) { idx, acc, num -> acc * selector(idx, num) }
}

operator fun Char.plus(chars: List<Char>): List<Char> {
    return buildList { add(this@plus); addAll(chars) }
}