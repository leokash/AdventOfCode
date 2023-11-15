
package com.github.leokash.adventofcode.utils.math

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.ln

fun safeLog(value: Double, eps: Double = 1e-08): Double {
    return ln(if (value >= eps) value else eps)
}

private val fmt = DecimalFormat()
fun Number.asString(decimals: Int = 2, rounding: RoundingMode = RoundingMode.HALF_EVEN): String {
    return fmt
        .apply { roundingMode = rounding; applyPattern("#.".padEnd(2 + decimals, '#')) }
        .format(this)
}
