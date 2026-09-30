package me._lisik.game

interface GameInstance {

    val serverId: String

    fun install()

    fun update()

    fun launch()
}