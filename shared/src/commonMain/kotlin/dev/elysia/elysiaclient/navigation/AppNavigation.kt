package dev.elysia.elysiaclient.navigation

import androidx.compose.runtime.*
import dev.elysia.elysiaclient.pages.ServersPage
import dev.elysia.elysiaclient.pages.SettingsPage

@Composable
fun AppNavigation() {
    var currentPage by remember {
        mutableStateOf<AppPage>(AppPage.Servers)
    }

    when (currentPage) {
        AppPage.Servers -> {
            ServersPage(
                onSettings = {
                    currentPage = AppPage.Settings
                },
                onLogout = {
                    // TODO: logout
                }
            )
        }

        AppPage.Settings -> {
            SettingsPage(
                onBack = {
                    currentPage = AppPage.Servers
                },
                onLogout = {
                    // TODO: logout
                }
            )
        }

        else -> {}
    }
}