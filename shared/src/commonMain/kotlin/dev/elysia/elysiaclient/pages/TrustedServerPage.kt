package dev.elysia.elysiaclient.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.theme.ElysiaMuted
import dev.elysia.elysiaclient.theme.ElysiaText

@Composable
fun TrustedServerPage(
    serverId: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = serverId,
            color = ElysiaText,
            fontSize = 28.sp,
        )

        Text(
            text = "Trusted Server Page",
            color = ElysiaMuted,
            fontSize = 14.sp,
        )
    }
}