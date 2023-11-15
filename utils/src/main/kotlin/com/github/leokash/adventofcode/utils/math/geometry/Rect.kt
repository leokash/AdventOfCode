
package com.github.leokash.adventofcode.utils.math.geometry

import com.github.leokash.adventofcode.utils.math.context.Context
import java.util.Objects

data class Rect<T>(val x: T, val y: T, val width: T, val height: T, private val context: Context<T>) where T: Number, T: Comparable<T> {
    private val corners: List<Point<T>> = with(context) {
        buildList {
            add(Point(x, y, context))
            add(Point(x, add(x, width), context))
            add(Point(x, add(x, height), context))
            add(Point(add(x, width), add(y, height), context))
        }
    }
    
    override fun hashCode(): Int = Objects.hash(x, y, width, height)
    override fun toString(): String = "Rect(x: $x, y: $y, width: $width, height: $height)"
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rect<*>) return false
        return x == other.x && y == other.y && width == other.width && height == other.height
    }
    
    infix fun intersects(other: Rect<T>): Boolean {
        return other.corners.any { contains(it) }
    }
    operator fun contains(other: Rect<T>): Boolean = with(context) {
        return x <= other.x
               && y >= other.y
               && add(x, width) >= add(other.x, other.width)
               && add(y, height) >= add(other.y, other.height)
    }
    operator fun contains(point: Point<*>): Boolean = with(context) {
        point.x in corners[0] && point.y in Point(y, add(y, height), context)
    }
    
    companion object {
        inline operator fun <reified T> invoke(x: T, y: T, w: T, h: T): Rect<T> where T: Number, T: Comparable<T> {
            return Rect(x, y, w, h, Context())
        }
    }
}

private operator fun <T> T.contains(point: Point<*>): Boolean where T: Number, T: Comparable<T> {
    return point.contains(this)
}
