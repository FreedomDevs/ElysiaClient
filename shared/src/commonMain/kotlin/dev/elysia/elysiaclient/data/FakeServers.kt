package dev.elysia.elysiaclient.data

import dev.elysia.elysiaclient.model.Server

val fakeServers = listOf(
    Server(
        id = "elysium-smp",
        name = "ElysiumSMP",
        description = "Fantasy • Steampunk • MMO",
        version = "1.26.2",
        players = 249,
        maxPlayers = 300,
        online = true,
    ),

    Server(
        id = "dead-cats",
        name = "Dead Cats",
        description = "Survival, zero gay",
        version = "1.26.2",
        players = -1,
        maxPlayers = 52,
        online = true,
    ),

    Server(
        id = "partner-network",
        name = "Partner Network",
        description = "Новый сервер скоро появится в Elysia",
        version = "1.26.2",
        players = 0,
        maxPlayers = 100,
        online = false,
    ),
)