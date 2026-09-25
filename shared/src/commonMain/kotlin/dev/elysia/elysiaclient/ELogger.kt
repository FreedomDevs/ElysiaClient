package dev.elysia.elysiaclient

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ELogger {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

    enum class Level {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    var level: Level = Level.INFO

    fun trace(message: String) = log(Level.TRACE, message)
    fun debug(message: String) = log(Level.DEBUG, message)
    fun info(message: String) = log(Level.INFO, message)
    fun warn(message: String) = log(Level.WARN, message)
    fun error(message: String) = log(Level.ERROR, message)

    fun error(message: String, throwable: Throwable) {
        log(Level.ERROR, "$message: ${throwable.message}")
        throwable.printStackTrace()
    }

    private fun log(
        messageLevel: Level,
        message: String
    ) {
        if (messageLevel.ordinal < level.ordinal) return

        val time = LocalDateTime.now().format(formatter)
        val thread = Thread.currentThread().name

        println(
            "$time [EC | ${messageLevel.name}] [$thread] $message"
        )
    }
}