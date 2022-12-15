

fun Int.makeEven(roundUp: Boolean = true): Int {
    return when (this % 2 == 0) {
        true -> this
        else -> if (roundUp) this + 1 else this -1
    }
}
fun Int.toPoint(rows: Int, columns: Int): Point {
    return Point(this / rows, this % columns)
}
