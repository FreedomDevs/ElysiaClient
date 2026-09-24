package dev.elysia.elysiaclient.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.theme.*

@Composable
fun ServersHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "Серверы",
                color = ElysiaText,
                fontSize = 30.sp,
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Выберите сервер и начните играть",
                color = ElysiaMuted,
                fontSize = 14.sp,
            )
        }
    }
}