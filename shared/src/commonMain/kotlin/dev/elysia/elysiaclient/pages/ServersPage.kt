package dev.elysia.elysiaclient.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.elysia.elysiaclient.components.ServerCard
import dev.elysia.elysiaclient.components.ServersHeader
import dev.elysia.elysiaclient.components.Sidebar
import dev.elysia.elysiaclient.data.fakeServers
import dev.elysia.elysiaclient.navigation.AppPage

@Composable
fun ServersPage(
    onSettings: () -> Unit,
    onLogout: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Sidebar(
            currentPage = AppPage.Servers,
            onServers = {},
            onSettings = onSettings,
            onLogout = onLogout,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
        ) {
            ServersHeader()

            Spacer(Modifier.height(30.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = fakeServers,
                    key = { it.id },
                ) { server ->
                    ServerCard(
                        server = server,
                        onPlay = {
                            println("Launching ${it.id}")
                        },
                    )
                }
            }
        }
    }
}