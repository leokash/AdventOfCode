
data class Rect(val x: Int, val y: Int, val width: Int, val height: Int) {
    operator fun contains(other: Rect): Boolean {
        return x <= other.x &&
                y >= other.y &&
                (x + width) >= (other.x + other.width)
                && (y + height) >= (other.y + other.height)
    }
    operator fun contains(point: Point): Boolean {
        return point.x in (x..(x + width)) && point.y in (y..(y + height))
    }
}
