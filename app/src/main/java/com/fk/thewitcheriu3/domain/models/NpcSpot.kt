package com.fk.thewitcheriu3.domain.models

import com.fk.thewitcheriu3.domain.models.characters.Character

data class NpcSpot(
    val id: Int,
    val type: SpotType,
    var isOccupied: Boolean = false,
    var occupiedBy: Character? = null,
    var occupationEndTime: Long = 0,
)

enum class SpotType {
    BED,
    TABLE,
    BAR,
}

enum class ActivityType {
    SLEEPING,
    EATING,
}