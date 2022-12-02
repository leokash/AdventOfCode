package collections

fun <T> Collection<T>.chunkBy(predicate: (T) -> Boolean): List<List<T>> = buildList {
    var container: MutableList<T>? = null
    this@chunkBy.forEach { value ->
        container = if (predicate(value)) {
            mutableListOf<T>().also { add(it) }
        } else {
            (container ?: mutableListOf<T>().also { add(it) }).also { it.add(value) }
        }
    }
}
