package dev.elysia.elysiaclient.api.event

interface EventBus {
    fun register(listener: Any)
    fun unregister(listener: Any)
    fun post(event: Event)
}