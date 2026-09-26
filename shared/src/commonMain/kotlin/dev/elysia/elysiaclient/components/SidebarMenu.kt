package dev.elysia.elysiaclient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.theme.ElysiaAccent
import dev.elysia.elysiaclient.theme.ElysiaMuted
import dev.elysia.elysiaclient.theme.ElysiaSurface
import dev.elysia.elysiaclient.theme.ElysiaText

@Composable
fun SidebarMenu(
    onServers: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(220.dp)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(16.dp),
            )
            .clip(RoundedCornerShape(16.dp))
            .background(ElysiaSurface.copy(alpha = 0.98f))
            .padding(8.dp),
    ) {
        SidebarMenuItem(
            icon = Icons.Default.Language,
            title = "Все серверы",
            onClick = onServers,
        )

        SidebarMenuItem(
            icon = Icons.Default.Settings,
            title = "Настройки",
            onClick = onSettings,
        )

        Spacer(Modifier.height(4.dp))

        SidebarMenuItem(
            icon = Icons.Default.Logout,
            title = "Выйти из аккаунта",
            destructive = true,
            onClick = onLogout,
        )
    }
}

@Composable
private fun SidebarMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    destructive: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(
                horizontal = 10.dp,
                vertical = 11.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = if (destructive) ElysiaAccent else ElysiaMuted,
        )

        Spacer(Modifier.width(11.dp))

        Text(
            text = title,
            color = if (destructive) ElysiaAccent else ElysiaText,
            fontSize = 13.sp,
        )
    }
}