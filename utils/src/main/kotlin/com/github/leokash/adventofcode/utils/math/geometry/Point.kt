
package com.github.leokash.adventofcode.utils.math.geometry

import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.compareTo
import java.util.Objects

typealias IntPoint = Point<Int>

data class Point<T>(var x: T, var y: T, val context: Context<T>) where T: Number, T: Comparable<T> {
    operator fun div(num: T): Point<T> = with(context) { Point(div(x, num), div(y, num), this) }
    operator fun plus(num: T): Point<T> = with(context) { Point(add(x, num), add(y, num), this) }
    operator fun times(num: T): Point<T> = with(context) { Point(mul(x, num), mul(y, num), this) }
    operator fun minus(num: T): Point<T> = with(context) { Point(sub(x, num), sub(y, num), this) }

    operator fun div(other: Point<T>): Point<T> = with(context) { Point(div(x, other.x), div(y, other.y), this) }
    operator fun plus(other: Point<T>): Point<T> = with(context) { Point(add(x, other.x), add(y, other.y), this) }
    operator fun times(other: Point<T>): Point<T> = with(context) { Point(mul(x, other.x), mul(y, other.y), this) }
    operator fun minus(other: Point<T>): Point<T> = with(context) { Point(sub(x, other.x), sub(y, other.y), this) }
    
    operator fun unaryPlus() {
        with(context) { x = add(x, one); y = add(y, one) }
    }
    
    operator fun unaryMinus() {
        with(context) { x = add(x, neg(one)); y = add(y, neg(one)) }
    }
    
    operator fun divAssign(other: Point<T>) {
        with(context) { x = div(x, other.x); y = div(y, other.y) }
    }
    
    operator fun plusAssign(other: Point<T>) {
        with(context) { x = add(x, other.x); y = add(y, other.y) }
    }
    
    operator fun minusAssign(other: Point<T>) {
        with(context) { x = sub(x, other.x); y = sub(y, other.y) }
    }
    
    operator fun timesAssign(other: Point<T>) {
        with(context) { x = mul(x, other.x); y = mul(y, other.y) }
    }
    
    override fun hashCode(): Int = Objects.hash(x, y)
    override fun toString(): String = "Point(x: $x, y: $y)"
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point<*>) return false
        return x == other.x && y == other.y
    }
    
    operator fun compareTo(other: Point<T>): Int = when(val result = x.compareTo(other.x)) {
        0 -> y.compareTo(other.y)
        else -> result
    }
    
    operator fun <T2: Number> contains(num: T2): Boolean = x <= num && num <= y
    
    companion object {
        inline operator fun <reified T> invoke(x: T, y: T): Point<T> where T: Number, T: Comparable<T> {
            return Point(x, y, Context())
        }
    }
}
