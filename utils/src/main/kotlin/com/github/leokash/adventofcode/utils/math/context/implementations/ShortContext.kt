
package com.github.leokash.adventofcode.utils.math.context.implementations

import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.safeLog
import kotlin.math.pow

internal class ShortContext : Context<Short> {
    override val one: Short = 1
    override val zero: Short = 0

    override fun abs(value: Short): Short = map(kotlin.math.abs(value.toInt()))
    override fun exp(value: Short): Short = map(kotlin.math.exp(value.toFloat()))
    override fun log(value: Short): Short = map(safeLog(value.toDouble()))
    override fun neg(value: Short): Short = map(-value.toInt())
    override fun sqrt(value: Short): Short = map(kotlin.math.sqrt(value.toFloat()))
    override fun pow(value: Short, power: Int): Short = map(value.toFloat().pow(power))

    override fun min(lhs: Short, rhs: Short): Short = map(kotlin.math.min(lhs.toInt(), rhs.toInt()))
    override fun max(lhs: Short, rhs: Short): Short = map(kotlin.math.max(lhs.toInt(), rhs.toInt()))
    override fun rem(lhs: Short, rhs: Short): Short = map(lhs % rhs)

    override fun eq(lhs: Short, rhs: Short): Boolean = lhs == rhs

    override fun map(value: Int): Short = value.toShort()
    override fun map(value: Long): Short = value.toShort()
    override fun map(value: Float): Short = map(value.toInt())
    override fun map(value: Short): Short = value
    override fun map(value: Double): Short = map(value.toInt())

    override fun add(lhs: Short, rhs: Short): Short = map(lhs.toInt() + rhs.toInt())
    override fun div(lhs: Short, rhs: Short): Short = map(lhs.toInt() / rhs.toInt())
    override fun mul(lhs: Short, rhs: Short): Short = map(lhs.toInt() * rhs.toInt())
}
