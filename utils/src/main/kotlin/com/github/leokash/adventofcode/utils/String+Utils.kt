
package com.github.leokash.adventofcode.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.min

fun String.isLong(): Boolean = this.toLongOrNull() != null

operator fun String.component1(): Char = this[0]
operator fun String.component2(): Char = this[1]
operator fun String.component3(): Char = this[2]
operator fun String.component4(): Char = this[3]
operator fun String.component5(): Char = this[4]

@Suppress("unused")
fun String.md5(): String {
    return BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
}

fun String.findAll(regex: Regex): List<String> {
    return findMatches(regex).map { (_, match) -> match }
}

fun String.findMatches(regex: Regex): List<Pair<IntRange,String>> {
    return buildList {
        var match = regex.find(this@findMatches)
        while (match != null) {
            add(match.range to match.value)
            match = match.next()
        }
    }
}

fun String.matchingGroups(regex: Regex): List<String> {
    return regex.find(this)?.groupValues?.drop(1) ?: emptyList()
}

fun String.removeAll(vararg list: String): String {
    var res = this
    list.forEach { value -> res = res.replace(value, "") }
    return res
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
