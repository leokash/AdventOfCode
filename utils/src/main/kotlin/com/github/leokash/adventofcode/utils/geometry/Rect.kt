package com.github.leokash.adventofcode.utils.geometry

import com.github.leokash.adventofcode.utils.geometry.points.Points
import com.github.leokash.adventofcode.utils.geometry.points.doubles.Point

data class Rect(val x: Double, val y: Double, val width: Double, val height: Double) {
    constructor(x: Int, y: Int, w: Int, h: Int): this(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
    constructor(x: Long, y: Long, w: Long, h: Long): this(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())

    infix fun intersects(other: Rect): Boolean {
        return other.corners.any { contains(it) }
    }
    operator fun contains(other: Rect): Boolean {
        return x <= other.x &&
                y >= other.y &&
                (x + width) >= (other.x + other.width)
                && (y + height) >= (other.y + other.height)
    }
    operator fun contains(point: Points.Interface<*>): Boolean {
        return point.x.toDouble() in (x..(x + width)) && point.y.toDouble() in (y..(y + height))
    }

    private val corners: List<Point> get() = buildList {
        add(Point(x, y))
        add(Point(x, x + width))
        add(Point(x, x + height))
        add(Point(x + width, y + height))
    }
}
