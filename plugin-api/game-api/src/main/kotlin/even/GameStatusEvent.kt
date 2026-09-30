package me._lisik.game.even

import me._lisik.game.GameInstance
import dev.elysia.elysiaclient.api.event.Event

data class GameStatusEvent(
    val instance: GameInstance,
    val message: String
) : Event