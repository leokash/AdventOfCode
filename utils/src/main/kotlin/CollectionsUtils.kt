

inline fun <T: Comparable<T>, R> List<T>.comparableFold(initial: R, operation: (acc: R, T, T) -> R): R {
    return when (size) {
        0, 1 -> initial
        else -> {
            var accumulator = initial
            forEachIndexed { idx, num ->
                accumulator = when (idx) {
                    0 -> accumulator
                    else -> operation(accumulator, this[idx - 1], num)
                }
            }
            return accumulator
        }
    }
}
