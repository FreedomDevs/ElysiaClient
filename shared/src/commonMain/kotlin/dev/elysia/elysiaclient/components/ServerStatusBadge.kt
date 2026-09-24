package dev.elysia.elysiaclient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.theme.*

@Composable
fun ServerStatusBadge(
    online: Boolean,
) {
    val color = if (online) {
        ElysiaGreen
    } else {
        ElysiaMuted
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.10f))
            .padding(
                horizontal = 7.dp,
                vertical = 3.dp,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = if (online) {
                    "Онлайн"
                } else {
                    "Скоро"
                },
                color = color,
                fontSize = 10.sp,
            )
        }
    }
}