package dev.elysia.elysiaclient

import dev.elysia.elysiaclient.api.ElysiaPlugin
import dev.elysia.elysiaclient.api.ElysiaPluginManifest
import dev.elysia.elysiaclient.api.PluginContext
import dev.elysia.elysiaclient.pages.ElysiumSMPPage

class ElysiumSMPPlugin : ElysiaPlugin {
    override val manifest = ElysiaPluginManifest(
        id = "elysium-smp",
        name = "Elysium SMP",
        version = "1.0.0",
        apiVersion = "1",
        description = "Офицальный плагин ElysiumSMP",
        author = "ElysiaCloud",
    )

    override fun onLoad(context: PluginContext) {
        context.registerPage(
            ElysiumSMPPage()
        )

        context.setDefaultPage(
            "elysium-smp:main"
        )
    }
}
