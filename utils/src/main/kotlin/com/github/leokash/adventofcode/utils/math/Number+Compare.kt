
package com.github.leokash.adventofcode.utils.math

operator fun <T: Number> Int.compareTo(value: T): Int = when(value::class) {
    Int::class -> compareTo(value)
    Long::class -> compareTo(value)
    Float::class -> compareTo(value)
    Short::class -> compareTo(value)
    Double::class -> compareTo(value) 
        
    else -> error("${value::class} not supported")
}

operator fun <T: Number> Long.compareTo(value: T): Int = when(value::class) {
    Int::class -> compareTo(value)
    Long::class -> compareTo(value)
    Float::class -> compareTo(value)
    Short::class -> compareTo(value)
    Double::class -> compareTo(value) 

    else -> error("${value::class} not supported")
}

operator fun <T: Number> Float.compareTo(value: T): Int = when(value::class) {
    Int::class -> compareTo(value)
    Long::class -> compareTo(value)
    Float::class -> compareTo(value)
    Short::class -> compareTo(value)
    Double::class -> compareTo(value) 

    else -> error("${value::class} not supported")
}

operator fun <T: Number> Short.compareTo(value: T): Int = when(value::class) {
    Int::class -> compareTo(value)
    Long::class -> compareTo(value)
    Float::class -> compareTo(value)
    Short::class -> compareTo(value)
    Double::class -> compareTo(value) 

    else -> error("${value::class} not supported")
}

operator fun <T: Number> Double.compareTo(value: T): Int = when(value::class) {
    Int::class -> compareTo(value)
    Long::class -> compareTo(value)
    Float::class -> compareTo(value)
    Short::class -> compareTo(value)
    Double::class -> compareTo(value) 

    else -> error("${value::class} not supported")
}

operator fun <T1: Number, T2: Number> T1.compareTo(value: T2): Int = when(this::class) {
    Int::class -> compareTo(value)
    Long::class -> compareTo(value)
    Float::class -> compareTo(value)
    Short::class -> compareTo(value)
    Double::class -> compareTo(value) 

    else -> error("${value::class} not supported")
}
