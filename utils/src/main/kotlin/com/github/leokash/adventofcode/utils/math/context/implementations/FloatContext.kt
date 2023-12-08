package com.github.leokash.adventofcode.utils.math.context.implementations

import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.safeLog
import kotlin.math.pow

internal class FloatContext : Context<Float> {
    override val one: Float = 1f
    override val zero: Float = 0f

    override fun abs(value: Float): Float = kotlin.math.abs(value)
    override fun exp(value: Float): Float = kotlin.math.exp(value)
    override fun log(value: Float): Float = map(safeLog(value.toDouble()))
    override fun neg(value: Float): Float = -value
    override fun sqrt(value: Float): Float = kotlin.math.sqrt(value)
    override fun pow(value: Float, power: Int): Float = value.pow(power)

    override fun min(lhs: Float, rhs: Float): Float = kotlin.math.min(lhs, rhs)
    override fun max(lhs: Float, rhs: Float): Float = kotlin.math.max(lhs, rhs)
    override fun rem(lhs: Float, rhs: Float): Float = lhs % rhs

    override fun eq(lhs: Float, rhs: Float): Boolean = lhs == rhs

    override fun map(value: Int): Float = value.toFloat()
    override fun map(value: Long): Float = value.toFloat()
    override fun map(value: Float): Float = value
    override fun map(value: Short): Float = value.toFloat()
    override fun map(value: Double): Float = value.toFloat()

    override fun add(lhs: Float, rhs: Float): Float = lhs + rhs
    override fun div(lhs: Float, rhs: Float): Float = lhs / rhs
    override fun mul(lhs: Float, rhs: Float): Float = lhs * rhs
}
