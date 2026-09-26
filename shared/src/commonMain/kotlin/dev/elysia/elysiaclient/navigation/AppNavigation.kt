package dev.elysia.elysiaclient.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.elysia.elysiaclient.AuthManager
import dev.elysia.elysiaclient.ELogger
import dev.elysia.elysiaclient.components.Sidebar
import dev.elysia.elysiaclient.pages.AuthLoadingPage
import dev.elysia.elysiaclient.pages.AuthPage
import dev.elysia.elysiaclient.pages.ServersPage
import dev.elysia.elysiaclient.pages.SettingsPage
import dev.elysia.elysiaclient.plugins.PluginManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.net.URI

@Composable
fun AppNavigation() {
    var isAuthorized by remember {
        mutableStateOf<Boolean?>(null)
    }

    val pluginManager = remember {
        PluginManager()
    }

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
        null -> AuthLoadingPage()

        false -> {
            AuthPage(
                onAuthorize = ::openAuthPage,
            )
        }

        true -> {
            AuthorizedApp(pluginManager)
        }
    }
}

@Composable
private fun AuthorizedApp(
    pluginManager: PluginManager,
) {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            pluginManager.loadPlugins()
        }
    }

    if (!pluginManager.isLoaded) {
        AuthLoadingPage()
        return
    }

    var currentPage by remember {
        mutableStateOf<AppPage?>(null)
    }

    val initialPage = remember {
        pluginManager.registry.defaultPageId
            ?.let { AppPage.Plugin(it) }
            ?: AppPage.Servers
    }

    LaunchedEffect(initialPage) {
        if (currentPage == null) {
            currentPage = initialPage
        }
    }

    val page = currentPage ?: return

    val servers = pluginManager.registry.servers

    val selectedServer = when (page) {
        is AppPage.TrustedServer -> page.serverId
        else -> null
    }

    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        Sidebar(
            trustedServers = servers,
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
                val server = pluginManager.registry
                    .getServer(page.serverId)

                if (server != null) {
                    val pluginPage = pluginManager.registry
                        .getPage(server.pageId)

                    if (pluginPage != null) {
                        pluginPage.Content(
                            context = PluginPageContextImpl(
                                navigate = { target ->
                                    currentPage = target
                                },
                            ),
                        )
                    } else {
                        ServersPage()
                    }
                } else {
                    ServersPage()
                }
            }

            is AppPage.Plugin -> {
                val pluginPage = pluginManager.registry
                    .getPage(page.pageId)

                if (pluginPage != null) {
                    pluginPage.Content(
                        context = PluginPageContextImpl(
                            navigate = { target ->
                                currentPage = target
                            },
                        ),
                    )
                } else {
                    ServersPage()
                }
            }

            AppPage.Login -> Unit
        }
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