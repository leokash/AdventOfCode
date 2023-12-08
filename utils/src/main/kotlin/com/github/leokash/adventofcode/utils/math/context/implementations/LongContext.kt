
package com.github.leokash.adventofcode.utils.math.context.implementations

import com.github.leokash.adventofcode.utils.math.context.Context
import com.github.leokash.adventofcode.utils.math.safeLog
import kotlin.math.pow

internal class LongContext : Context<Long> {
    override val one: Long = 1L
    override val zero: Long = 0L

    override fun abs(value: Long): Long = kotlin.math.abs(value)
    override fun exp(value: Long): Long = map(kotlin.math.exp(value.toDouble()))
    override fun log(value: Long): Long = map(safeLog(value.toDouble()))
    override fun neg(value: Long): Long = -value
    override fun sqrt(value: Long): Long = map(kotlin.math.sqrt(value.toDouble()))
    override fun pow(value: Long, power: Int): Long = map(value.toDouble().pow(power))

    override fun min(lhs: Long, rhs: Long): Long = kotlin.math.min(lhs, rhs)
    override fun max(lhs: Long, rhs: Long): Long = kotlin.math.max(lhs, rhs)
    override fun rem(lhs: Long, rhs: Long): Long = lhs % rhs

    override fun eq(lhs: Long, rhs: Long): Boolean = lhs == rhs

    override fun map(value: Int): Long = value.toLong()
    override fun map(value: Long): Long = value
    override fun map(value: Float): Long = value.toLong()
    override fun map(value: Short): Long = value.toLong()
    override fun map(value: Double): Long = value.toLong()

    override fun add(lhs: Long, rhs: Long): Long = lhs + rhs
    override fun div(lhs: Long, rhs: Long): Long = lhs / rhs
    override fun mul(lhs: Long, rhs: Long): Long = lhs * rhs
}
