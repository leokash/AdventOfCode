
@Suppress("ComplexMethod")
data class Point(var x: Int = 0, var y: Int = 0) {
    fun direction(to: Point): Direction? {
        return if (this == to) null else when {
            x == to.x -> if (y < to.y) Direction.WEST else Direction.EAST
            y == to.y -> if (x < to.x) Direction.SOUTH else Direction.NORTH
            else -> {
                if (x < to.x)
                    if (y < to.y) Direction.SOUTH_WEST else Direction.SOUTH_EAST
                else
                    if (y < to.y) Direction.NORTH_WEST else Direction.NORTH_EAST
            }
        }
    }
    fun next(direction: Direction, bounds: Rect? = null): Point? {
        fun take(point: Point): Boolean {
            return bounds == null || point in bounds
        }

        return when (direction) {
            Direction.NORTH -> Point(x - 1, y).let { if (take(it)) it else null }
            Direction.NORTH_EAST -> Point(x - 1, y - 1).let { if (take(it)) it else null }
            Direction.EAST -> Point(x, y - 1).let { if (take(it)) it else null }
            Direction.SOUTH_EAST -> Point(x + 1, y - 1).let { if (take(it)) it else null }
            Direction.SOUTH -> Point(x + 1, y).let { if (take(it)) it else null }
            Direction.SOUTH_WEST -> Point(x + 1, y + 1).let { if (take(it)) it else null }
            Direction.WEST -> Point(x, y + 1).let { if (take(it)) it else null }
            Direction.NORTH_WEST -> Point(x - 1, y + 1).let { if (take(it)) it else null }
        }
    }
    fun move(steps: Int = 1, direction: Direction, bounds: Rect? = null): Point {
        var start = this
        repeat(steps) {
            start = start.next(direction, bounds) ?: return start
        }

        return start
    }
}
