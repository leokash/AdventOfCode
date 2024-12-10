
package com.github.leokash.adventofcode.utils.collections

import com.github.leokash.adventofcode.utils.math.mapInts
import com.github.leokash.adventofcode.utils.math.mapLongs
import com.github.leokash.adventofcode.utils.matrix.CharMatrix
import com.github.leokash.adventofcode.utils.matrix.IntMatrix

fun List<String>.mapInts(delimiter: Regex = ",".toRegex()): List<List<Int>> = map { it.mapInts(delimiter) }
fun List<String>.mapLongs(delimiter: Regex = ",".toRegex()): List<List<Long>> = map { it.mapLongs(delimiter) }

fun List<String>.toIntMatrix(map: (Int, Int, Char) -> Int = {_, _, c -> c.digitToInt() }): IntMatrix {
    return IntMatrix(size, this[0].length) { x, y ->  map(x, y, this[x][y]) }
}

fun List<String>.toCharMatrix(map: (Int, Int, Char) -> Char = {_, _, c -> c }): CharMatrix {
    return CharMatrix(size, this[0].length) { x, y ->  map(x, y, this[x][y]) }
}
