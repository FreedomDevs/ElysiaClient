package dev.elysia.elysiaclient.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elysia.elysiaclient.plugins.PluginManager
import dev.elysia.elysiaclient.api.ElysiaPlugin
import dev.elysia.elysiaclient.theme.ElysiaAccent
import dev.elysia.elysiaclient.theme.ElysiaMuted
import dev.elysia.elysiaclient.theme.ElysiaSurface
import dev.elysia.elysiaclient.theme.ElysiaText

@Composable
fun PluginsPage(
    pluginManager: PluginManager,
) {
    val plugins = pluginManager.registry.plugins

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 32.dp,
                vertical = 28.dp,
            ),
    ) {
        Text(
            text = "Плагины",
            color = ElysiaText,
            fontSize = 26.sp,
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "${plugins.size} установлено",
            color = ElysiaMuted,
            fontSize = 13.sp,
        )

        Spacer(Modifier.height(24.dp))

        if (plugins.isEmpty()) {
            EmptyPlugins()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = plugins,
                    key = { it.manifest.id },
                ) { plugin ->
                    PluginItem(plugin)
                }
            }
        }
    }
}

@Composable
private fun PluginItem(
    plugin: ElysiaPlugin,
) {
    val manifest = plugin.manifest

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ElysiaSurface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(42.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(
                    ElysiaAccent.copy(alpha = 0.12f)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Extension,
                contentDescription = null,
                tint = ElysiaAccent,
                modifier = Modifier
                    .width(21.dp)
                    .height(21.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = manifest.name,
                color = ElysiaText,
                fontSize = 15.sp,
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = manifest.description.ifBlank {
                    "Без описания"
                },
                color = ElysiaMuted,
                fontSize = 12.sp,
                maxLines = 2,
            )

            Spacer(Modifier.height(7.dp))

            Row {
                Text(
                    text = "v${manifest.version}",
                    color = ElysiaMuted,
                    fontSize = 11.sp,
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = manifest.author.ifBlank {
                        "Неизвестный автор"
                    },
                    color = ElysiaMuted,
                    fontSize = 11.sp,
                )
            }
        }
    }
}

@Composable
private fun EmptyPlugins() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.Extension,
                contentDescription = null,
                tint = ElysiaMuted,
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Плагинов пока нет",
                color = ElysiaText,
                fontSize = 15.sp,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Установленные плагины появятся здесь",
                color = ElysiaMuted,
                fontSize = 12.sp,
            )
        }
    }
}