
fun Int.toPoint(rows: Int, columns: Int): Point {
    return Point(this / rows, this % columns)
}
