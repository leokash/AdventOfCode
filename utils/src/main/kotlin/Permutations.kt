
fun <T> List<T>.permutations(): List<T> {
    return mapIndexed { i, lhs ->
        drop(i + 1).map { rhs ->
            listOf(lhs, rhs, rhs, lhs)
        }.flatten()
    }.flatten()
}
