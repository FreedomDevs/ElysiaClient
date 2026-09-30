package me._lisik.game

interface GameAPI {

    fun instance(serverId: String): GameInstance
}