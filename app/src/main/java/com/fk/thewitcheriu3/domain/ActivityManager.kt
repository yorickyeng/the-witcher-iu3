package com.fk.thewitcheriu3.domain

import com.fk.thewitcheriu3.domain.models.ActivityType
import com.fk.thewitcheriu3.domain.models.NpcSpot
import com.fk.thewitcheriu3.domain.models.SpotType
import com.fk.thewitcheriu3.domain.models.characters.Character
import kotlin.random.Random

class ActivityManager {
    private val tavernBeds = List(7) { NpcSpot(it, SpotType.BED) }
    private val tavernTables = List(7) { NpcSpot(it, SpotType.TABLE) }
    private val kaerMorhenBeds = List(5) { NpcSpot(it + 10, SpotType.BED) }
    private val kaerMorhenTables = List(6) { NpcSpot(it + 20, SpotType.TABLE) }

    private val activeActivities = mutableMapOf<Character, Pair<ActivityType, Long>>()
    private val lastOccupationTimes = mutableMapOf<Int, Long>()

    fun getAvailableSpots(location: String, type: SpotType): List<NpcSpot> {
        return when (location) {
            "tavern" -> if (type == SpotType.BED) tavernBeds else tavernTables
            "Kaer Morhen" -> if (type == SpotType.BED) kaerMorhenBeds else kaerMorhenTables
            else -> emptyList()
        }
    }

    fun occupySpot(spot: NpcSpot, character: Character, activityType: ActivityType, duration: Long) {
        spot.isOccupied = true
        spot.occupiedBy = character
        spot.occupationEndTime = duration
        activeActivities[character] = Pair(activityType, duration)
        lastOccupationTimes[spot.id] = duration
    }

    fun releaseSpot(spot: NpcSpot) {
        spot.isOccupied = false
        spot.occupiedBy = null
        spot.occupationEndTime = 0
    }

    fun updateSpots(currentTime: Long) {
        (tavernBeds + tavernTables + kaerMorhenBeds + kaerMorhenTables).forEach { spot ->
            if (spot.isOccupied && currentTime >= spot.occupationEndTime) {
                spot.occupiedBy?.let { character ->
                    when (spot.type) {
                        SpotType.BED -> character.health += 50
                        SpotType.TABLE -> character.moveRange += 1
                        SpotType.BAR -> {}
                    }
                    activeActivities.remove(character)
                }
                releaseSpot(spot)
            }
        }
    }

    fun getCharacterActivity(character: Character): Pair<ActivityType, Long>? {
        return activeActivities[character]
    }

    fun isCharacterActive(character: Character): Boolean {
        return activeActivities.containsKey(character)
    }

    fun getSpotOccupationTime(spot: NpcSpot, currentTime: Long): String {
        if (!spot.isOccupied) {
            val lastOccupation = lastOccupationTimes[spot.id] ?: 0L
            val timeSinceLastOccupation = currentTime - lastOccupation
            if (timeSinceLastOccupation < 300) {
                val remainingProtection = 300 - timeSinceLastOccupation
                val minutes = remainingProtection / 60
                val seconds = remainingProtection % 60
                return "Available (protection ${minutes}m ${seconds}s)"
            }
            return "Available"
        }
        val remainingTime = spot.occupationEndTime - currentTime
        val hours = remainingTime / 60
        val minutes = remainingTime % 60
        return "Occupied (${hours}h ${minutes}m)"
    }

    fun randomlyOccupySpots(currentTime: Long) {
        (tavernBeds + tavernTables + kaerMorhenBeds + kaerMorhenTables).forEach { spot ->
            if (!spot.isOccupied) {
                val lastOccupation = lastOccupationTimes[spot.id] ?: 0L
                val timeSinceLastOccupation = currentTime - lastOccupation

                if (timeSinceLastOccupation >= 300 && Random.nextFloat() < 0.1f) {
                    spot.isOccupied = true
                    spot.occupationEndTime = currentTime + Random.nextLong(60, 180)
                }
            }
        }
    }
}