
package com.github.leokash.adventofcode.utils

import com.github.leokash.adventofcode.utils.math.geometry.Point
import kotlin.math.min
import kotlin.math.max

infix fun IntRange.intersects(rhs: IntRange): Boolean {
    for (i in (min(first, rhs.first))..(max(last, rhs.last)))
        if (i in this && i in rhs)
            return true
    return false
}

operator fun <T: Comparable<T>> ClosedRange<T>.contains(rhs: ClosedRange<T>): Boolean {
    return start <= rhs.start && endInclusive >= rhs.endInclusive
}

operator fun IntRange.contains(point: Point<Int>): Boolean {
    return point.x in this && point.y in this
}
operator fun LongRange.contains(point: Point<Long>): Boolean {
    return point.x in this && point.y in this
}
