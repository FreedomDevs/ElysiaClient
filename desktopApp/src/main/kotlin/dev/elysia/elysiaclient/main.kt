package dev.elysia.elysiaclient

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.elysia.elysiaclient.plugins.PluginManager

fun main() {
    if (!AuthManager.init()) {
        return
    }

    val pluginManager = PluginManager()

    pluginManager.loadPlugins()

    application {
        Window(
            onCloseRequest = {
                pluginManager.disablePlugins()
                exitApplication()
            },
            title = "ElysiaClient",
        ) {
            App(
            )
        }
    }
}