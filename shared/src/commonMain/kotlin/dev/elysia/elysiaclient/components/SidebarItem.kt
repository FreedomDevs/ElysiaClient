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
import dev.elysia.elysiaclient.theme.*

@Composable
fun SidebarItem(
    icon: String,
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background = if (selected) {
        ElysiaAccent.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(
                horizontal = 12.dp,
                vertical = 11.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = icon,
            color = if (selected) {
                ElysiaAccent
            } else {
                ElysiaMuted
            },
            fontSize = 17.sp,
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = title,
            color = if (selected) {
                ElysiaText
            } else {
                ElysiaMuted
            },
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.Medium
            } else {
                FontWeight.Normal
            },
        )
    }
}