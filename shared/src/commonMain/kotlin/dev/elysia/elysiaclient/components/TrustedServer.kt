package dev.elysia.elysiaclient.components

import androidx.compose.runtime.Composable

data class TrustedServer(
    val id: String,
    val name: String,
    val iconUrl: String,

    val page: @Composable () -> Unit,

    val isInitial: Boolean = false,
)