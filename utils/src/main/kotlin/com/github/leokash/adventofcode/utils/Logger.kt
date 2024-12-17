
@file:Suppress("unused")

package com.github.leokash.adventofcode.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import java.io.File

fun interface Logger {
    fun log(message: String)

    companion object Config {
        var enabled: Boolean = false
        val default: Logger = Logger {
            if (enabled)
                println(it)
        }
    }
}

fun Logger.withThreadName() = Logger {
    log("${Thread.currentThread().name} - $it")
}

fun Logger.withFileLogging(filename: String? = null) = Logger {
    if (!Logger.enabled)
        return@Logger

    val format = LocalDateTime.Format {
        dayOfMonth()
        char('-')
        monthNumber()
        char('-')
        year()
    }

    log(it)
    File(filename ?: "${format.format(Clock.System.now().toLocalDateTime(TimeZone.of("GMT")))}.log").appendText(it + "\n")
}

fun log(logger: Logger = Logger.default, message: () -> String) {
    logger.log(message())
}

fun <T> T.log(logger: Logger = Logger.default, message: T.() -> String): T {
    logger.log(message())
    return this
}
