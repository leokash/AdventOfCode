
package com.github.leokash.adventofcode.utils.math.context

fun <T> Context<T>.lcm(lhs: T, rhs: T): T where T: Number, T: Comparable<T> {
    val res = mul(lhs, rhs)
    val max = max(lhs, rhs)
    var lcm = max
    while (lcm <= res) {
        if (rem(lcm, lhs) == zero && rem(lcm, rhs) == zero)
            return lcm

        lcm = add(max, lcm)
    }

    return max
}

fun <T> Context<T>.gcd(lhs: T, rhs: T): T where T: Number, T: Comparable<T> {
    var t: T
    var a = lhs
    var b = rhs
    while (b != zero) {
        t = rem(a, b)
        a = b
        b = t
    }

    return abs(a)
}
