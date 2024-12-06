
package com.github.leokash.adventofcode.utils

class Logger private constructor() {
    companion object {
        var debug: Boolean = false

        fun log(message: () -> String) {
            if (!debug) return
            println(message())
        }
    }
}

fun log(message: () -> String) {
    Logger.log(message)
}

fun <T> T.log(message: T.() -> String): T {
    Logger.log { message() }
    return this
}
