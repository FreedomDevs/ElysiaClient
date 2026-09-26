package dev.elysia.elysiaclient.api.navigation

interface PluginPageContext {

    fun navigateTo(pageId: String)

    fun navigateToServer(serverId: String)
}