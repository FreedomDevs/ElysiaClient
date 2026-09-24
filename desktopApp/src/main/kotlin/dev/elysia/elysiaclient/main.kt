package dev.elysia.elysiaclient

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ElysiaClient",
    ) {
        App()
    }
}