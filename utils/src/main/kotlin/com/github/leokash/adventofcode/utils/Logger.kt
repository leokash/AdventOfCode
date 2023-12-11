
package com.github.leokash.adventofcode.utils

interface LogListener {
    fun logged(message: String)
}

class Logger private constructor() {
    companion object {
        var debug: Boolean = false
        private val listeners = mutableListOf<LogListener>()

        fun log(message: () -> String) {
            message().let { m ->
                if (debug) println(m)
                listeners.onEach { it.logged(m) }
            }
        }

        fun addListener(l: LogListener) {
            listeners.add(l)
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
