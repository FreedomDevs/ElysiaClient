package dev.elysia.elysiaclient.api

import dev.elysia.elysiaclient.api.event.EventBus
import dev.elysia.elysiaclient.api.navigation.PluginPage
import dev.elysia.elysiaclient.api.server.TrustedServer
import me._lisik.game.GameAPI

interface PluginContext {

    val plugin: ElysiaPlugin

    val logger: PluginLogger

    val game: GameAPI

    val events: EventBus

    fun registerServer(server: TrustedServer)

    fun registerPage(
        page: PluginPage
    )

    fun setDefaultPage(
        pageId: String
    )
}