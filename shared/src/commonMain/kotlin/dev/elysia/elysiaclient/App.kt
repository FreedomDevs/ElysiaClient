package dev.elysia.elysiaclient

import androidx.compose.runtime.Composable
import dev.elysia.elysiaclient.navigation.AppNavigation
import dev.elysia.elysiaclient.theme.ElysiaTheme

@Composable
fun App(
) {
    ElysiaTheme {
        AppNavigation(
        )
    }
}