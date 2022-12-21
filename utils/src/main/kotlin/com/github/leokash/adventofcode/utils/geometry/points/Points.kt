
@file:Suppress("UNCHECKED_CAST")

package com.github.leokash.adventofcode.utils.geometry.points

import com.github.leokash.adventofcode.utils.Direction
import com.github.leokash.adventofcode.utils.geometry.Rect

class Points private constructor() {
    interface Interface<T: Number> {
        val x: T
        val y: T
    }
    interface Provider<T: Provider<T>> {
        fun next(direction: Direction, bounds: Rect? = null): T?
        operator fun invoke(direction: Direction, bounds: Rect? = null): T? = next(direction, bounds)
    }

    companion object {
        fun <T: Comparable<T>> direction(x1: T, y1: T, x2: T, y2: T): Direction? {
            return if (x1 == x2 && y1 == y2) null else when {
                x1 == x2 -> if (y1 < y2) Direction.EAST else Direction.WEST
                y1 == y2 -> if (x1 < x2) Direction.SOUTH else Direction.NORTH
                else -> {
                    if (x1 < x2)
                        if (y1 < y2) Direction.SOUTH_EAST else Direction.SOUTH_WEST
                    else
                        if (y1 < y2) Direction.NORTH_EAST else Direction.NORTH_WEST
                }
            }
        }
        fun <P: Provider<P>> move(initial: P, steps: Int, direction: Direction, bounds: Rect? = null): P {
            var start = initial
            repeat(steps) {
                start = start(direction, bounds) ?: return start
            }

            return start
        }
        fun <P: Provider<P>> neighbors(point: P, acceptOrdinals: Boolean = false, bounds: Rect? = null): List<Pair<Direction, P>> {
            return buildList {
                addAll(Direction.cardinals.mapNotNull { d -> point(d, bounds)?.let { d to it } })
                if (acceptOrdinals) addAll(Direction.ordinals.mapNotNull { d -> point(d, bounds)?.let { d to it } })
            }
        }
    }
}
