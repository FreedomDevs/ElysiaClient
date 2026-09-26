package dev.elysia.elysiaclient.api

interface ElysiaPlugin {

    val manifest: ElysiaPluginManifest

    fun onLoad(context: PluginContext)

    fun onEnable() {}

    fun onDisable() {}
}