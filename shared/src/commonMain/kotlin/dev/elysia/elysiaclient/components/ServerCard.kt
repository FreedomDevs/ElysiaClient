package dev.elysia.elysiaclient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.model.Server
import dev.elysia.elysiaclient.theme.*

@Composable
fun ServerCard(
    server: Server,
    onPlay: (Server) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ElysiaSurface)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.045f),
                shape = RoundedCornerShape(16.dp),
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Server icon

        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    if (server.online) {
                        ElysiaAccent.copy(alpha = 0.12f)
                    } else {
                        ElysiaSurfaceLight
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = server.name
                    .firstOrNull()
                    ?.uppercase()
                    ?: "?",
                color = if (server.online) {
                    ElysiaAccent
                } else {
                    ElysiaMuted
                },
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.width(18.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = server.name,
                    color = ElysiaText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(Modifier.width(10.dp))

                ServerStatusBadge(
                    online = server.online,
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = server.description,
                color = ElysiaMuted,
                fontSize = 12.sp,
            )

            Spacer(Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = server.version,
                    color = ElysiaMuted,
                    fontSize = 11.sp,
                )

                if (server.online) {
                    Spacer(Modifier.width(15.dp))

                    Text(
                        text = "${server.players}/${server.maxPlayers} игроков",
                        color = ElysiaMuted,
                        fontSize = 11.sp,
                    )
                }
            }
        }

        Spacer(Modifier.width(20.dp))

        Button(
            onClick = {
                onPlay(server)
            },
            enabled = server.online,
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElysiaAccent,
                contentColor = Color.White,
                disabledContainerColor = ElysiaSurfaceLight,
                disabledContentColor = ElysiaMuted,
            ),
        ) {
            Text(
                text = if (server.online) {
                    "Играть"
                } else {
                    "Скоро"
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}