
package com.github.leokash.adventofcode.utils.geometry.points.doubles

import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.abs
import com.github.leokash.adventofcode.utils.geometry.Rect
import com.github.leokash.adventofcode.utils.geometry.points.Points
import kotlin.math.*

data class Point(override var x: Double, override var y: Double): Points.Interface<Double>, Points.Provider<Point> {
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
        val steps = (this cDist other).toInt()
        return (1..steps).scan(this) { p, _ -> Point(p.x + xd, p.y + yd) }
    }

    fun direction(to: Point): Direction? {
        return Points.direction(x, y, to.x, to.y)
    }
    override fun next(direction: Direction, bounds: Rect?): Point? {
        val d = direction.point.let { Point(it.x.toDouble(), it.y.toDouble()) }
        return (this + d).let { new -> if (bounds == null || new in bounds) new else null }
    }
    fun move(steps: Int = 1, direction: Direction, bounds: Rect? = null): Point {
        return Points.move(this, steps, direction, bounds)
    }
    fun neighbors(acceptOrdinals: Boolean = false, bounds: Rect? = null): List<Pair<Direction, Point>> {
        return Points.neighbors(this, acceptOrdinals, bounds)
    }

    infix fun cDist(rhs: Point): Double = chebyshevDistance(rhs)
    infix fun eDist(rhs: Point): Double = euclideanDistance(rhs)
    infix fun mDist(rhs: Point): Double = manhattanDistance(rhs)

    fun chebyshevDistance(rhs: Point): Double {
        return max((x - rhs.x).abs, (y - rhs.y).abs)
    }
    fun euclideanDistance(rhs: Point): Double {
        return sqrt((x.toDouble() - rhs.x).pow(2).abs + (y.toDouble() - rhs.y).pow(2).abs)
    }
    fun manhattanDistance(rhs: Point): Double {
        return (x - rhs.x).abs + (y - rhs.y).abs
    }
}
