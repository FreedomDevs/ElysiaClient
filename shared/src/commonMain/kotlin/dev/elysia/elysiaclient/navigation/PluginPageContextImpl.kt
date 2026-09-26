package dev.elysia.elysiaclient.navigation

import dev.elysia.elysiaclient.api.navigation.PluginPageContext

class PluginPageContextImpl(
    private val navigate: (AppPage) -> Unit,
) : PluginPageContext {

    override fun navigateTo(pageId: String) {
        navigate(AppPage.Plugin(pageId))
    }

    override fun navigateToServer(serverId: String) {
        navigate(AppPage.TrustedServer(serverId))
    }
}