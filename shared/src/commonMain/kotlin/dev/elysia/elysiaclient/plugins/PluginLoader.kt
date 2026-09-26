package dev.elysia.elysiaclient.plugins

import dev.elysia.elysiaclient.api.ElysiaPlugin
import java.io.File
import java.net.URLClassLoader

class PluginLoader {

    fun load(file: File): LoadedPlugin {
        require(file.extension.equals("jar", ignoreCase = true)) {
            "Plugin file must be a JAR: ${file.name}"
        }

        val classLoader = URLClassLoader(
            arrayOf(file.toURI().toURL()),
            javaClass.classLoader,
        )

        val manifest = readManifest(classLoader)

        val pluginClass = classLoader.loadClass(
            manifest.mainClass
        )

        require(
            ElysiaPlugin::class.java.isAssignableFrom(pluginClass)
        ) {
            "Main class '${manifest.mainClass}' " +
                    "does not implement ElysiaPlugin"
        }

        val constructor = pluginClass.getDeclaredConstructor()
        val plugin = constructor.newInstance() as ElysiaPlugin

        require(plugin.manifest.id == manifest.id) {
            "Plugin manifest mismatch in '${file.name}'"
        }

        return LoadedPlugin(
            plugin = plugin,
            file = file,
            classLoader = classLoader,
        )
    }

    private fun readManifest(
        classLoader: ClassLoader,
    ): PluginDescriptor {
        val resource = classLoader.getResourceAsStream(
            "elysia-plugin.properties"
        ) ?: throw IllegalStateException(
            "Missing elysia-plugin.properties"
        )

        val properties = java.util.Properties()

        resource.use {
            properties.load(it)
        }

        return PluginDescriptor(
            id = properties.require("id"),
            name = properties.require("name"),
            version = properties.require("version"),
            apiVersion = properties.require("apiVersion"),
            mainClass = properties.require("mainClass"),
        )
    }

    private fun java.util.Properties.require(
        key: String,
    ): String {
        return getProperty(key)
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException(
                "Missing '$key' in elysia-plugin.properties"
            )
    }

    private data class PluginDescriptor(
        val id: String,
        val name: String,
        val version: String,
        val apiVersion: String,
        val mainClass: String,
    )
}