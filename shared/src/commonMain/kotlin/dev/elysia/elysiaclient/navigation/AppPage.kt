package dev.elysia.elysiaclient.navigation

sealed interface AppPage {
    data object Login : AppPage
    data object Servers : AppPage
    data object Settings : AppPage
}