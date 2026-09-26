package dev.elysia.testplugin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.api.ElysiaPlugin
import dev.elysia.elysiaclient.api.ElysiaPluginManifest
import dev.elysia.elysiaclient.api.PluginContext
import dev.elysia.elysiaclient.api.navigation.PluginPage
import dev.elysia.elysiaclient.api.navigation.PluginPageContext
import dev.elysia.elysiaclient.api.server.TrustedServer

class TestPlugin : ElysiaPlugin {

    override val manifest = ElysiaPluginManifest(
        id = "test-plugin",
        name = "Test Plugin",
        version = "1.0.0",
        apiVersion = "0.1.0",
        description = "ElysiaClient plugin test",
        author = "Elysia",
    )

    override fun onLoad(context: PluginContext) {
        context.logger.info("Hello from TestPlugin!")

        context.registerPage(
            TestPage()
        )

        context.registerServer(
            TrustedServer(
                id = "test-server",
                name = "Test Server",
                iconUrl = "https://cdn.discordapp.com/attachments/1537503255851040838/1552772213537644655/server-icon.png?ex=6ab8cd90&is=6ab77c10&hm=3c1e1803ed790593942cf09cef406537ce44013fc44981049c18b5431156280e&",
                pageId = "test-plugin:server",
            )
        )

        context.setDefaultPage(
            "test-plugin:server"
        )
    }

    override fun onEnable() {
        println("TestPlugin enabled!")
    }

    override fun onDisable() {
        println("TestPlugin disabled!")
    }
}

class TestPage : PluginPage {

    override val id = "test-plugin:server"

    override val title = "Test Server"

    @Composable
    override fun Content(
        context: PluginPageContext
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Hello from plugin!",
                fontSize = 32.sp,
                color = Color.White,
            )

            Text(
                text = "This page belongs to TestPlugin",
                color = Color.LightGray,
            )
        }
    }
}