
package com.github.leokash.adventofcode.utils

class Logger private constructor() {
    companion object {
        var debug: Boolean = false
        fun log(message: () -> String) {
            if (debug) { println(message()) }
        }

    }

}

fun log(message: () -> String) {
    Logger.log(message)
}
