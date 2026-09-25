package dev.elysia.elysiaclient.navigation

import androidx.compose.runtime.*
import dev.elysia.elysiaclient.AuthManager
import dev.elysia.elysiaclient.ELogger
import dev.elysia.elysiaclient.pages.AuthLoadingPage
import dev.elysia.elysiaclient.pages.AuthPage
import dev.elysia.elysiaclient.pages.ServersPage
import dev.elysia.elysiaclient.pages.SettingsPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.net.URI

@Composable
fun AppNavigation() {
    var isAuthorized by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        AuthManager.setOnAuthStateChanged {
            isAuthorized = AuthManager.isAuth()
        }

        withContext(Dispatchers.IO) {
            val authorized = AuthManager.isAuth()

            withContext(Dispatchers.Main) {
                isAuthorized = authorized
            }
        }
    }

    when (isAuthorized) {
        null -> {
            AuthLoadingPage()
        }

        true -> {
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
                            if (AuthManager.logOut()) {
                                isAuthorized = false
                            }
                        }
                    )
                }

                AppPage.Settings -> {
                    SettingsPage(
                        onBack = {
                            currentPage = AppPage.Servers
                        },
                        onLogout = {
                            if (AuthManager.logOut()) {
                                isAuthorized = false
                            }
                        }
                    )
                }

                else -> {}
            }
        }

        false -> {
            AuthPage(
                onAuthorize = ::openAuthPage
            )
        }
    }
}

private fun openAuthPage() {
    try {
        Desktop.getDesktop().browse(
            URI.create("https://sso.elysiac.fun/auth?client_id=elysia_client")
        )

    } catch (e: Exception) {
        ELogger.error("Failed to open authentication page: ${e.message}")
    }
}