package com.github.leokash.adventofcode.utils.math

fun String.mapInts(splitter: Regex): List<Int> = this.trim().split(splitter).map(String::toInt)
fun String.mapLongs(splitter: Regex): List<Long> = this.trim().split(splitter).map(String::toLong)
