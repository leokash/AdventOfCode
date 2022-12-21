
package com.github.leokash.adventofcode.utils.geometry.points.ints

import com.github.leokash.adventofcode.utils.abs
import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.geometry.Rect
import com.github.leokash.adventofcode.utils.geometry.points.Points
import kotlin.math.*

data class Point(override var x: Int = 0, override var y: Int = 0): Points.Interface<Int>, Points.Provider<Point> {
    operator fun div(rhs: Point): Point {
        return Point(x / rhs.x, y / rhs.y)
    }
    operator fun plus(rhs: Point): Point {
        return Point(x + rhs.x, y + rhs.y)
    }
    operator fun minus(rhs: Point): Point {
        return Point(x - rhs.x, y - rhs.y)
    }
    operator fun times(rhs: Point): Point {
        return Point(x * rhs.x, y * rhs.y)
    }

    fun lineTo(other: Point): List<Point> {
        val xd = (other.x - x).sign
        val yd = (other.y - y).sign
        val steps = this cDist other
        return (1..steps).scan(this) { p, _ -> Point(p.x + xd, p.y + yd) }
    }

    fun direction(to: Point): Direction? {
        return Points.direction(x, y, to.x, to.y)
    }
    override fun next(direction: Direction, bounds: Rect?): Point? {
        return with(this + direction.point) {
            if (bounds == null || this in bounds) this else null
        }
    }
    fun move(steps: Int = 1, direction: Direction, bounds: Rect? = null): Point {
        return Points.move(this, steps, direction, bounds)
    }
    fun neighbors(acceptOrdinals: Boolean = false, bounds: Rect? = null): List<Pair<Direction, Point>> {
        return Points.neighbors(this, acceptOrdinals, bounds)
    }

    infix fun cDist(rhs: Point): Int = chebyshevDistance(rhs)
    infix fun eDist(rhs: Point): Int = euclideanDistance(rhs)
    infix fun mDist(rhs: Point): Int = manhattanDistance(rhs)

    fun chebyshevDistance(rhs: Point): Int {
        return max((x - rhs.x).abs, (y - rhs.y).abs)
    }
    fun euclideanDistance(rhs: Point): Int {
        return sqrt((x.toDouble() - rhs.x).pow(2).abs + (y.toDouble() - rhs.y).pow(2).abs).toInt()
    }
    fun manhattanDistance(rhs: Point): Int {
        return (x - rhs.x).abs + (y - rhs.y).abs
    }
}
