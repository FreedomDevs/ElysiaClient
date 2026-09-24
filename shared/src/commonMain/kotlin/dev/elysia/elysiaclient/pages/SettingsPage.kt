package dev.elysia.elysiaclient.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.components.Sidebar
import dev.elysia.elysiaclient.navigation.AppPage
import dev.elysia.elysiaclient.theme.*

@Composable
fun SettingsPage(
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Sidebar(
            currentPage = AppPage.Settings,
            onServers = onBack,
            onSettings = {},
            onLogout = onLogout,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
        ) {
            Text(
                text = "Настройки",
                color = ElysiaText,
                fontSize = 30.sp,
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Настройте ElysiaClient под себя",
                color = ElysiaMuted,
                fontSize = 14.sp,
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Общие",
                color = ElysiaText,
                fontSize = 16.sp,
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = "Настройки Minecraft появятся здесь.",
                color = ElysiaMuted,
                fontSize = 13.sp,
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = "← Назад к серверам",
                color = ElysiaAccent,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onBack()
                    }
            )
        }
    }
}