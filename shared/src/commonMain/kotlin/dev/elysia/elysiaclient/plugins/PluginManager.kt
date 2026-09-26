package dev.elysia.elysiaclient.plugins

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.elysia.elysiaclient.AppPaths
import dev.elysia.elysiaclient.ELogger
import java.io.File

class PluginManager {

    private val pluginsDirectory = AppPaths.pluginsDirectory.toFile()

    val registry = PluginRegistry()

    private val loader = PluginLoader()

    private val loadedPlugins = mutableListOf<LoadedPlugin>()

    var isLoaded by mutableStateOf(false)
        private set

    fun loadPlugins() {
        if (isLoaded) {
            return
        }

        if (!pluginsDirectory.exists()) {
            pluginsDirectory.mkdirs()
        }

        val files = pluginsDirectory
            .listFiles()
            ?.filter {
                it.isFile &&
                        it.extension.equals("jar", ignoreCase = true)
            }
            ?.sortedBy { it.name }
            ?: emptyList()

        ELogger.info("Found ${files.size} plugin(s)")

        files.forEach(::loadPlugin)

        isLoaded = true
    }

    private fun loadPlugin(file: File) {
        try {
            ELogger.info("Loading plugin: ${file.name}")

            val loaded = loader.load(file)

            registry.registerPlugin(loaded.plugin)

            val context = PluginContextImpl(
                plugin = loaded.plugin,
                registry = registry,
            )

            loaded.plugin.onLoad(context)
            loaded.plugin.onEnable()

            loadedPlugins += loaded

            ELogger.info(
                "Plugin '${loaded.plugin.manifest.id}' enabled"
            )
        } catch (e: Exception) {
            ELogger.error(
                "Failed to load plugin '${file.name}': ${e.message}"
            )

            e.printStackTrace()
        }
    }

    fun disablePlugins() {
        loadedPlugins
            .asReversed()
            .forEach { loaded ->
                try {
                    loaded.plugin.onDisable()
                } catch (e: Exception) {
                    ELogger.error(
                        "Failed to disable plugin " +
                                "'${loaded.plugin.manifest.id}': ${e.message}"
                    )
                }

                try {
                    (loaded.classLoader as? java.net.URLClassLoader)?.close()
                } catch (e: Exception) {
                    ELogger.error(
                        "Failed to close classloader: ${e.message}"
                    )
                }
            }

        loadedPlugins.clear()
        isLoaded = false
    }
}