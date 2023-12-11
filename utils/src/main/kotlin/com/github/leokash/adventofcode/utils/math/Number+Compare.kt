
package com.github.leokash.adventofcode.utils.math

operator fun <T: Number> Int.compareTo(value: T): Int = compareTo(value.toInt())
operator fun <T: Number> Long.compareTo(value: T): Int = compareTo(value.toLong())
operator fun <T: Number> Float.compareTo(value: T): Int = compareTo(value.toFloat())
operator fun <T: Number> Short.compareTo(value: T): Int = compareTo(value.toShort())
operator fun <T: Number> Double.compareTo(value: T): Int = compareTo(value.toDouble())

operator fun <T1: Number, T2: Number> T1.compareTo(value: T2): Int = when(this::class) {
    Int::class -> (this as Int).compareTo(value)
    Long::class -> (this  as Long).compareTo(value)
    Float::class -> (this  as Float).compareTo(value)
    Short::class -> (this  as Short).compareTo(value)
    Double::class -> (this  as Double).compareTo(value)

    else -> error("${value::class} not supported")
}
