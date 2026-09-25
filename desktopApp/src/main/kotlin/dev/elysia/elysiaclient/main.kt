package dev.elysia.elysiaclient

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    if (!AuthManager.init()) {
        return
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ElysiaClient",
        ) {
            App()
        }
    }
}