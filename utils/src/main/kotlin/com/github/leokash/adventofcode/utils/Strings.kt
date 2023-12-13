
package com.github.leokash.adventofcode.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.max
import kotlin.math.min

operator fun String.component1(): Char = this[0]
operator fun String.component2(): Char = this[1]
operator fun String.component3(): Char = this[2]
operator fun String.component4(): Char = this[3]
operator fun String.component5(): Char = this[4]

fun String.md5(): String {
    return BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
}

fun String.findAll(regex: Regex): List<String> {
    return buildList {
        var match = regex.find(this@findAll)
        while (match != null) {
            add(match.value)
            match = match.next()
        }
    }
}

fun String.matchingGroups(regex: Regex): List<String> {
    return regex.find(this)?.groupValues?.drop(1) ?: emptyList()
}

fun String.replace(data: Map<String, String>): String {
    var res = this
    data.forEach { (old, new) ->  res = res.replace(old, new) }
    return res
}

operator fun String.get(range: IntRange): String {
    val start = range.first
    if (start < 0 || start >= length) return ""
    return substring(range.first, min(length, range.last))
}
