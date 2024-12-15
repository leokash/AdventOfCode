
@file:Suppress("unused")

package com.github.leokash.adventofcode.utils.matrix

import com.github.leokash.adventofcode.utils.math.geometry.Point

typealias IntMatrix = Matrix<Int>
typealias CharMatrix = Matrix<Char>
typealias LongMatrix = Matrix<Long>

fun Matrix.Factory.ofInts(rows: Int, cols: Int, init: (Int, Int) -> Int = {_, _ -> 0 }): IntMatrix {
    return Matrix(rows, cols, init)
}

fun Matrix.Factory.ofLongs(rows: Int, cols: Int, init: (Int, Int) -> Long = {_, _ -> 0 }): LongMatrix {
    return Matrix(rows, cols, init)
}

fun Matrix.Factory.ofChars(rows: Int, cols: Int, defaultChar: Char = ' ', init: (Int, Int) -> Char = {_, _ -> defaultChar }): CharMatrix {
    return Matrix(rows, cols, init)
}

fun <T, R> List<List<T>>.asMatrix(map: (T) -> R): Matrix<R> = Matrix(this.size, this[0].size) { x, y -> map(this[x][y]) }

fun <R> List<String>.stringsToMatrix(map: (Char) -> R): Matrix<R> = stringsToMatrix { _, c -> map(c) }
fun <R> List<String>.stringsToMatrix(map: (Point<Int>, Char) -> R): Matrix<R> = Matrix(this.size, this[0].length) { x, y ->
    map(Point(x, y), this[x][y])
}