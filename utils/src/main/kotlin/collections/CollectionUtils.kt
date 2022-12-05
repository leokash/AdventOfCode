package collections

fun <T> Collection<T>.chunkedBy(predicate: (T) -> Boolean): List<List<T>> = buildList {
    var container: MutableList<T>? = null
    this@chunkedBy.forEach { value ->
        container = if (predicate(value)) {
            mutableListOf<T>().also { add(it) }
        } else {
            (container ?: mutableListOf<T>().also { add(it) }).also { it.add(value) }
        }
    }
}
