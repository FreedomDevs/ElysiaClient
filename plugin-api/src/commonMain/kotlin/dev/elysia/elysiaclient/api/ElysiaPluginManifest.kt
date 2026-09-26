package dev.elysia.elysiaclient.api

data class ElysiaPluginManifest(
    val id: String,
    val name: String,
    val version: String,
    val apiVersion: String,
    val description: String = "",
    val author: String = "",
)