
data class Point(var x: Int = 0, var y: Int = 0) {
    fun next(dir: Direction, bounds: Rect? = null): Point? {
        fun take(point: Point): Boolean {
            return bounds == null || point in bounds
        }

        return when (dir) {
            Direction.East -> Point(x, y - 1).let { if (take(it)) it else null }
            Direction.West -> Point(x, y + 1).let { if (take(it)) it else null }
            Direction.North -> Point(x - 1, y).let { if (take(it)) it else null }
            Direction.South -> Point(x + 1, y).let { if (take(it)) it else null }
        }
    }
}
