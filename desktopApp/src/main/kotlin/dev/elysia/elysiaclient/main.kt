package dev.elysia.elysiaclient

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.elysia.elysiaclient.api.server.TrustedServer
import dev.elysia.elysiaclient.pages.ElysiumSMPPage
import dev.elysia.elysiaclient.plugins.PluginManager

fun main() {
    if (!AuthManager.init()) {
        return
    }

    val pluginManager = PluginManager()

    pluginManager.registry.registerBuiltinPage(ElysiumSMPPage())
    pluginManager.registry.registerBuiltinServer(
        TrustedServer(
            id = "elysium-smp",
            name = "Elysium SMP",
            iconUrl = "https://i.pinimg.com/originals/bb/10/93/bb10933979e794bbc3f697bee286b58a.gif",
            pageId = "elysium-smp:main",
        )
    )

    pluginManager.loadPlugins()

    pluginManager.registry.setDefaultPage("elysium-smp:main")

    application {
        Window(
            onCloseRequest = {
                pluginManager.disablePlugins()
                exitApplication()
            },
            title = "ElysiaClient",
        ) {
            App(pluginManager)
        }
    }
}