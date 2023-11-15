
package com.github.leokash.adventofcode.utils.math.context.implementations

import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.safeLog
import kotlin.math.pow

internal class IntContext : Context<Int> {
    override val one: Int = 1
    override val zero: Int = 0
    
    override fun abs(value: Int): Int = kotlin.math.abs(value)
    override fun exp(value: Int): Int = map(kotlin.math.exp(value.toFloat()))
    override fun log(value: Int): Int = map(safeLog(value.toDouble()))
    override fun neg(value: Int): Int = -value
    override fun sqrt(value: Int): Int = map(kotlin.math.sqrt(value.toFloat()))
    override fun pow(value: Int, power: Int): Int = map(value.toFloat().pow(power))

    override fun min(lhs: Int, rhs: Int): Int = kotlin.math.min(lhs, rhs)
    override fun max(lhs: Int, rhs: Int): Int = kotlin.math.max(lhs, rhs)
    
    override fun eq(lhs: Int, rhs: Int): Boolean = lhs == rhs
    
    override fun map(value: Int): Int = value
    override fun map(value: Long): Int = value.toInt()
    override fun map(value: Float): Int = value.toInt()
    override fun map(value: Short): Int = value.toInt()
    override fun map(value: Double): Int = value.toInt()
    
    override fun add(lhs: Int, rhs: Int): Int = lhs + rhs
    override fun div(lhs: Int, rhs: Int): Int = lhs / rhs
    override fun mul(lhs: Int, rhs: Int): Int = lhs * rhs
}