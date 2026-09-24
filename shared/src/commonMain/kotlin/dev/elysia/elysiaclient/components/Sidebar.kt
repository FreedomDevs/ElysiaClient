package dev.elysia.elysiaclient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.navigation.AppPage
import dev.elysia.elysiaclient.theme.*
import androidx.compose.foundation.Image
import elysiaclient.shared.generated.resources.Res
import elysiaclient.shared.generated.resources.elysia_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun Sidebar(
    currentPage: AppPage,
    onServers: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(245.dp)
            .fillMaxHeight()
            .background(ElysiaSurface)
            .padding(18.dp),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 8.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(Res.drawable.elysia_logo),
                contentDescription = "ElysiaClient",
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )

            Spacer(Modifier.width(12.dp))

            Row {
                Text(
                    text = "Elysia",
                    color = ElysiaText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Client",
                    color = ElysiaAccent,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(Modifier.height(35.dp))

        SidebarItem(
            icon = "◈",
            title = "Серверы",
            selected = currentPage == AppPage.Servers,
            onClick = onServers,
        )

        Spacer(Modifier.height(4.dp))

        SidebarItem(
            icon = "⚙",
            title = "Настройки",
            selected = currentPage == AppPage.Settings,
            onClick = onSettings,
        )

        Spacer(Modifier.weight(1f))

        AccountCard(
            username = "_lisik",
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onLogout)
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "↪",
                color = ElysiaMuted,
                fontSize = 18.sp,
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = "Выйти из аккаунта",
                color = ElysiaMuted,
                fontSize = 13.sp,
            )
        }
    }
}