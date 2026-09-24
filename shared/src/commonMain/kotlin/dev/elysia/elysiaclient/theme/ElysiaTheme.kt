package dev.elysia.elysiaclient.theme

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box

private val ElysiaColorScheme = darkColorScheme(
    primary = ElysiaAccent,
    background = ElysiaBackground,
    surface = ElysiaSurface,
    onPrimary = ElysiaText,
    onBackground = ElysiaText,
    onSurface = ElysiaText,
)

@Composable
fun ElysiaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ElysiaColorScheme,
    ) {
        Box(
            modifier = Modifier
                .background(ElysiaBackground)
        ) {
            content()
        }
    }
}