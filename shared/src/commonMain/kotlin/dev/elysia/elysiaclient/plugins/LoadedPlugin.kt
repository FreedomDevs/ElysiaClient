package dev.elysia.elysiaclient.plugins

import dev.elysia.elysiaclient.api.ElysiaPlugin
import java.io.File

data class LoadedPlugin(
    val plugin: ElysiaPlugin,
    val file: File,
    val classLoader: ClassLoader,
)