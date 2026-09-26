package dev.elysia.elysiaclient.plugins

import dev.elysia.elysiaclient.ELogger
import dev.elysia.elysiaclient.api.PluginLogger

class PluginLoggerImpl(
    private val pluginId: String,
) : PluginLogger {

    override fun info(message: String) {
        ELogger.info("[$pluginId] $message")
    }

    override fun warn(message: String) {
        ELogger.warn("[$pluginId] $message")
    }

    override fun error(message: String) {
        ELogger.error("[$pluginId] $message")
    }
}