package dev.elysia.elysiaclient.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil3.compose.AsyncImage
import dev.elysia.elysiaclient.theme.ElysiaAccent
import dev.elysia.elysiaclient.theme.ElysiaMuted
import dev.elysia.elysiaclient.theme.ElysiaSurface
import elysiaclient.shared.generated.resources.Res
import elysiaclient.shared.generated.resources.elysia_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun Sidebar(
    trustedServers: List<TrustedServer>,
    selectedServer: String?,
    onServerClick: (TrustedServer) -> Unit,
    onServers: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(68.dp)
            .fillMaxHeight()
            .background(ElysiaSurface)
            .padding(
                vertical = 14.dp,
                horizontal = 10.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.elysia_logo),
            contentDescription = "ElysiaClient",
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp)),
        )

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            trustedServers.forEach { server ->
                TrustedServerItem(
                    server = server,
                    selected = server.id == selectedServer,
                    onClick = {
                        onServerClick(server)
                    },
                )

                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(12.dp))

        SidebarIconButton(
            onClick = {
                menuOpen = !menuOpen
            },
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Меню",
                modifier = Modifier.size(21.dp),
                tint = if (menuOpen) {
                    ElysiaAccent
                } else {
                    ElysiaMuted
                },
            )
        }
    }

    if (menuOpen) {
        Popup(
            alignment = Alignment.BottomStart,
            onDismissRequest = {
                menuOpen = false
            },
            offset = IntOffset(
                x = 76,
                y = -14,
            ),
        ) {
            AnimatedVisibility(
                visible = menuOpen,
                enter = fadeIn(
                    animationSpec = tween(180)
                ) +
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween(180)
                        ) +
                        slideInVertically(
                            initialOffsetY = { it / 10 },
                            animationSpec = tween(180)
                        ),
                exit = fadeOut(
                    animationSpec = tween(140)
                ) +
                        scaleOut(
                            targetScale = 0.92f,
                            animationSpec = tween(140)
                        ) +
                        slideOutVertically(
                            targetOffsetY = { it / 10 },
                            animationSpec = tween(140)
                        ),
            ) {
                SidebarMenu(
                    onServers = {
                        menuOpen = false
                        onServers()
                    },
                    onSettings = {
                        menuOpen = false
                        onSettings()
                    },
                    onLogout = {
                        menuOpen = false
                        onLogout()
                    },
                )
            }
        }
    }
}

@Composable
private fun TrustedServerItem(
    server: TrustedServer,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background = if (selected) {
        ElysiaAccent.copy(alpha = 0.14f)
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(5.dp),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = server.iconUrl,
            contentDescription = server.name,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp)),
            onLoading = {
                println("🖼 Loading: ${server.iconUrl}")
            },
            onSuccess = {
                println("🖼 Loaded: ${server.iconUrl}")
            },
            onError = {
                println("🖼 ERROR: ${server.iconUrl}")
                println("🖼 ${it.result.throwable}")
            },
        )
    }
}

@Composable
private fun SidebarIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(13.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}