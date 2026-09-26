package dev.elysia.elysiaclient.plugins

import dev.elysia.elysiaclient.api.ElysiaPlugin
import dev.elysia.elysiaclient.api.PluginContext
import dev.elysia.elysiaclient.api.PluginLogger
import dev.elysia.elysiaclient.api.navigation.PluginPage
import dev.elysia.elysiaclient.api.server.TrustedServer

class PluginContextImpl(
    override val plugin: ElysiaPlugin,
    private val registry: PluginRegistry,
) : PluginContext {

    override val logger: PluginLogger =
        PluginLoggerImpl(plugin.manifest.id)

    override fun registerServer(server: TrustedServer) {
        registry.registerServer(server)

        logger.info(
            "Registered trusted server '${server.id}'"
        )
    }

    override fun registerPage(page: PluginPage) {
        registry.registerPage(page)

        logger.info(
            "Registered page '${page.id}'"
        )
    }

    override fun setDefaultPage(pageId: String) {
        registry.setDefaultPage(pageId)

        logger.info(
            "Set default page '$pageId'"
        )
    }
}