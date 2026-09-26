package dev.elysia.elysiaclient.api.navigation

import androidx.compose.runtime.Composable

interface PluginPage {

    val id: String

    val title: String

    @Composable
    fun Content(
        context: PluginPageContext
    )
}