package com.github.leokash.adventofcode.utils.math.geometry

import com.github.leokash.adventofcode.utils.collections.sum

/*
    https://en.wikipedia.org/wiki/Pick%27s_theorem
    List needs to contain the vertices of the polygon
*/
fun <T> List<Point<T>>.area(): T where T: Number, T: Comparable<T> {
    assert(isNotEmpty())
    with(this[0].context) {
        return add(add(shoelace(), div(perimeter(), two)), one)
    }
}

/*
    https://en.wikipedia.org/wiki/Shoelace_formula
    Can contain all points making up the lines or just the vertices
*/
fun <T> List<Point<T>>.shoelace(): T where T: Number, T: Comparable<T> {
    assert(isNotEmpty())
    with(this[0].context) {
        val f = this@shoelace[0]
        val l = this@shoelace.last()
        return div(
            add(
                this@shoelace.windowed(2).sum(this) { (lhs, rhs) -> sub(mul(lhs.x, rhs.y), mul(rhs.x, lhs.y)) },
                sub(mul(l.x, f.y), mul(f.x, l.y))
            ),
            two
        )
    }
}

fun <T> List<Point<T>>.perimeter(): T where T: Number, T: Comparable<T> {
    assert(isNotEmpty())
    with(this[0].context) {
        return (this@perimeter + this@perimeter[0]).windowed(2).sum(this) { (lhs, rhs) ->
            lhs.manhattanDistance(rhs)
        }
    }
}
