
package com.github.leokash.adventofcode.utils.matrix

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