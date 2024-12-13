
package com.github.leokash.adventofcode.utils.matrix

import com.github.leokash.adventofcode.utils.math.geometry.Point
import kotlin.math.abs
import kotlin.math.max

class MatrixStringifier private constructor() {
    private class Spacer(var max: Int) {
        fun update(s: Int) {
            max = max(max, s)
        }
    }
    private class Padded(private val string: String, private val spacer: Spacer) {
        init {
            spacer.update(string.length)
        }
        override fun toString(): String {
            return string.padLeft(spacer.max)
        }
    }

    companion object {
        fun <T> stringify(mat: Matrix<T>,
                          indent: String = "",
                          separator: String = "",
                          transform: (T) -> String = { it.toString() }): String {
            return stringify(mat, indent, separator) {_, e -> transform(e) }
        }

        fun <T> stringify(mat: Matrix<T>,
                          indent: String = "",
                          separator: String = "",
                          transform: (Point<Int>, T) -> String): String {
            val spacers = mutableMapOf<Int, Spacer>()
            val stringsArr = mutableListOf<List<Padded>>()
            for (x in (0 until mat.rows))
                stringsArr += mat.row(x)
                    .mapIndexed { y, t -> Padded(transform(Point(x, y), t), spacers.getOrPut(y) { Spacer(0) }) }

            return stringsArr.joinToString (separator = "") { list ->
                list.joinToString (prefix = "$indent|", postfix = "|\n", separator = separator) { it.toString() }
            }
        }
    }
}

private fun String.padLeft( max: Int = 0) : String {
    if (max == 0 || this.length >= max)
        return this

    return "".padStart(abs(this.length - max)) + this
}
