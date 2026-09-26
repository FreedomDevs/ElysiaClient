package dev.elysia.elysiaclient.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.AuthManager
import dev.elysia.elysiaclient.ELogger
import dev.elysia.elysiaclient.components.Sidebar
import dev.elysia.elysiaclient.components.TrustedServer
import dev.elysia.elysiaclient.pages.AuthLoadingPage
import dev.elysia.elysiaclient.pages.AuthPage
import dev.elysia.elysiaclient.pages.ServersPage
import dev.elysia.elysiaclient.pages.SettingsPage
import dev.elysia.elysiaclient.theme.ElysiaMuted
import dev.elysia.elysiaclient.theme.ElysiaText
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

        false -> {
            AuthPage(
                onAuthorize = ::openAuthPage
            )
        }

        true -> {
            AuthorizedApp()
        }
    }
}

@Composable
private fun AuthorizedApp() {
    val trustedServers = remember {
        listOf(
            TrustedServer(
                id = "dead-cats",
                name = "Dead Cats",
                iconUrl = "https://cdn.discordapp.com/attachments/1537503255851040838/1552772213537644655/server-icon.png?ex=6ab8cd90&is=6ab77c10&hm=3c1e1803ed790593942cf09cef406537ce44013fc44981049c18b5431156280e&",

                page = {
                    DeadCatsPage()
                },

                isInitial = true,
            )
        )
    }

    val initialPage = remember(trustedServers) {
        trustedServers
            .firstOrNull { it.isInitial }
            ?.let { AppPage.TrustedServer(it.id) }
            ?: AppPage.Servers
    }

    var currentPage by remember {
        mutableStateOf<AppPage?>(null)
    }

    LaunchedEffect(initialPage) {
        if (currentPage == null) {
            currentPage = initialPage
        }
    }

    val page = currentPage ?: return

    val selectedServer = when (page) {
        is AppPage.TrustedServer -> page.serverId
        else -> null
    }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Sidebar(
            trustedServers = trustedServers,
            selectedServer = selectedServer,

            onServerClick = { server ->
                currentPage = AppPage.TrustedServer(server.id)
            },

            onServers = {
                currentPage = AppPage.Servers
            },

            onSettings = {
                currentPage = AppPage.Settings
            },

            onLogout = {
                AuthManager.logOut()
            },
        )

        when (page) {
            AppPage.Servers -> {
                ServersPage()
            }

            AppPage.Settings -> {
                SettingsPage()
            }

            is AppPage.TrustedServer -> {
                val server = trustedServers.find {
                    it.id == page.serverId
                }

                if (server != null) {
                    server.page()
                } else {
                    ServersPage()
                }
            }

            AppPage.Login -> {
            }
        }
    }
}

@Composable
private fun DeadCatsPage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "DeadCats",
            color = ElysiaText,
            fontSize = 28.sp,
        )

        Text(
            text = "Trusted Server Page",
            color = ElysiaMuted,
            fontSize = 14.sp,
        )
    }
}

private fun openAuthPage() {
    try {
        Desktop.getDesktop().browse(
            URI.create(
                "https://sso.elysiac.fun/auth?client_id=elysia_client"
            )
        )
    } catch (e: Exception) {
        ELogger.error(
            "Failed to open authentication page: ${e.message}"
        )
    }
}