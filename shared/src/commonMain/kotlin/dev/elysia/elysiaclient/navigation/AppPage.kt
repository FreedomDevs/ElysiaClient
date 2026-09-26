package dev.elysia.elysiaclient.navigation

sealed interface AppPage {
    data object Login : AppPage
    data object Servers : AppPage
    data object Settings : AppPage

    data class TrustedServer(
        val serverId: String,
    ) : AppPage

    data class Plugin(
        val pageId: String,
    ) : AppPage
}