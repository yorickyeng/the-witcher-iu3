package com.fk.thewitcheriu3.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val map: Array<Array<CellState>>,
    val player: HeroState,
    val computer: HeroState,
    val units: List<UnitState>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (!map.contentDeepEquals(other.map)) return false
        if (player != other.player) return false
        if (computer != other.computer) return false
        if (units != other.units) return false

        return true
    }

    override fun hashCode(): Int {
        var result = map.contentDeepHashCode()
        result = 31 * result + player.hashCode()
        result = 31 * result + computer.hashCode()
        result = 31 * result + units.hashCode()
        return result
    }
}

@Serializable
data class CellState(
    val x: Int,
    val y: Int,
    val hero: HeroState? = null,
    val unit: UnitState? = null,
    val type: String,
)

@Serializable
data class HeroState(
    val type: String,
    val name: String,
    val xCoord: Int,
    val yCoord: Int,
    val health: Int,
    val money: Int,
)

@Serializable
data class UnitState(
    val type: String,
    val health: Int,
    val xCoord: Int,
    val yCoord: Int,
)