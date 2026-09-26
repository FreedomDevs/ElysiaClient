package dev.elysia.elysiaclient.api

import dev.elysia.elysiaclient.api.navigation.PluginPage
import dev.elysia.elysiaclient.api.server.TrustedServer

interface PluginContext {

    val plugin: ElysiaPlugin

    val logger: PluginLogger

    fun registerServer(server: TrustedServer)

    fun registerPage(
        page: PluginPage
    )

    fun setDefaultPage(
        pageId: String
    )
}