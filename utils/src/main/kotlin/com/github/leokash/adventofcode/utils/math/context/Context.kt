
package com.github.leokash.adventofcode.utils.math.context

import com.github.leokash.adventofcode.utils.math.context.implementations.*
import kotlin.reflect.KClass

interface Context<T> where T: Number, T: Comparable<T> {
    val one: T
    val zero: T

    val two: T get() = add(one, one)

    fun abs(value: T): T
    fun exp(value: T): T
    fun log(value: T): T
    fun neg(value: T): T
    fun sqrt(value: T): T
    fun pow(value: T, power: Int = 2): T

    fun min(lhs: T, rhs: T): T
    fun max(lhs: T, rhs: T): T
    fun rem(lhs: T, rhs: T): T

    fun eq(lhs: T, rhs: T): Boolean
    fun neq(lhs: T, rhs: T): Boolean = !eq(lhs, rhs)
    
    fun sign(num: T): T = when {
        num > zero -> one
        num < zero -> neg(one)
        else -> zero
    }
    
    fun equals(lhs: T, rhs: T, tolerance: T): Boolean {
        val lower = sub(rhs, tolerance)
        val upper = add(rhs, tolerance)
        
        return lhs >= lower && rhs <= upper
    }
    
    fun map(value: Int): T
    fun map(value: Long): T
    fun map(value: Float): T
    fun map(value: Short): T
    fun map(value: Double): T
    
    fun map(value: Any): T = when(value::class) {
        Int::class -> map(value as Int)
        Long::class -> map(value as Long)
        Float::class -> map(value as Float)
        Short::class -> map(value as Short)
        Double::class -> map(value as Double)
        
        else -> error("${value::class} not supported")
    }

    fun add(lhs: T, rhs: T): T
    fun add(lhs: T, rhs: Any): T = add(lhs, map(rhs))
    
    fun mul(lhs: T, rhs: T): T
    fun mul(lhs: T, rhs: Any): T = mul(lhs, map(rhs))
    
    fun sub(lhs: T, rhs: T): T = add(lhs, neg(rhs))
    fun sub(lhs: T, rhs: Any): T = sub(lhs, map(rhs))
    
    fun div(lhs: T, rhs: T): T
    fun div(lhs: T, rhs: Any): T = div(lhs, map(rhs))
    
    companion object {
        inline operator fun <reified T> invoke(): Context<T> where T: Number, T: Comparable<T> {
            return contextOf(T::class)
        }

        private val cache = mutableMapOf<KClass<*>, Context<*>>()
        fun <T> contextOf(type: KClass<T>): Context<T>  where T: Number, T: Comparable<T> {
            @Suppress("UNCHECKED_CAST")
            return cache.getOrPut(type) {
                when (type) {
                    Int::class -> IntContext()
                    Long::class -> LongContext()
                    Float::class -> FloatContext()
                    Short::class -> ShortContext()
                    Double::class -> DoubleContext()

                    else -> error("Unsupported operator for type: $type")
                }
            } as Context<T>
        }
    }
}
