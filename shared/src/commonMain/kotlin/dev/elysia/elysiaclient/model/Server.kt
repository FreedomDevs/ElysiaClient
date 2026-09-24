package dev.elysia.elysiaclient.model

data class Server(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val players: Int,
    val maxPlayers: Int,
    val online: Boolean,
)