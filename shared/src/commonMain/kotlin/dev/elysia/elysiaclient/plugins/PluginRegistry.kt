package dev.elysia.elysiaclient.plugins

import androidx.compose.runtime.mutableStateListOf
import dev.elysia.elysiaclient.api.ElysiaPlugin
import dev.elysia.elysiaclient.api.navigation.PluginPage
import dev.elysia.elysiaclient.api.server.TrustedServer

class PluginRegistry {

    private val _plugins = mutableStateListOf<ElysiaPlugin>()
    private val _servers = mutableStateListOf<TrustedServer>()
    private val _pages = mutableStateListOf<PluginPage>()

    var defaultPageId: String? = null
        private set

    val plugins: List<ElysiaPlugin>
        get() = _plugins

    val servers: List<TrustedServer>
        get() = _servers

    val pages: List<PluginPage>
        get() = _pages

    fun registerPlugin(plugin: ElysiaPlugin) {
        if (_plugins.any { it.manifest.id == plugin.manifest.id }) {
            throw IllegalStateException(
                "Plugin '${plugin.manifest.id}' is already registered"
            )
        }

        _plugins += plugin
    }

    fun registerServer(server: TrustedServer) {
        if (_servers.any { it.id == server.id }) {
            throw IllegalStateException(
                "Trusted server '${server.id}' is already registered"
            )
        }

        _servers += server
    }

    fun registerPage(page: PluginPage) {
        if (_pages.any { it.id == page.id }) {
            throw IllegalStateException(
                "Plugin page '${page.id}' is already registered"
            )
        }

        _pages += page
    }

    fun setDefaultPage(pageId: String) {
        if (_pages.none { it.id == pageId }) {
            throw IllegalStateException(
                "Cannot set default page '$pageId': page is not registered"
            )
        }

        defaultPageId = pageId
    }

    fun getPage(pageId: String): PluginPage? {
        return _pages.firstOrNull { it.id == pageId }
    }

    fun getServer(serverId: String): TrustedServer? {
        return _servers.firstOrNull { it.id == serverId }
    }
}