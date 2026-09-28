package dev.elysia.elysiaclient.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.elysia.elysiaclient.api.navigation.PluginPage
import dev.elysia.elysiaclient.api.navigation.PluginPageContext

class ElysiumSMPPage : PluginPage {
    override val id = "elysium-smp:main"
    override val title = "ElysiumSMP"

    @Composable
    override fun Content(
        context: PluginPageContext
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("Elysium SMP")
        }
    }
}