
package matrix

import kotlin.math.abs
import kotlin.math.max

private fun String.padLeft( max: Int = 0) : String {
    if (max == 0 || this.length >= max)
        return this

    return "".padStart(abs(this.length - max)) + this
}

class MatrixStringifier {
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

    fun <T> stringify(array: Matrix<T>, indent: String = "", stringify: (T) -> String = { it.toString() }): String {
        val spacers = mutableMapOf<Int, Spacer>()
        val stringsArr = mutableListOf<List<Padded>>()
        for (i in (0 until array.rows))
            stringsArr += array.row(i)
                .mapIndexed { j, t -> Padded(stringify(t), spacers.getOrPut(j) { Spacer(0) }) }

        return stringsArr.joinToString (separator = "") { list ->
            list.joinToString (prefix = "$indent|", postfix = "|\n", separator = " ") { it.toString() }
        }
    }
}
