package com.fk.thewitcheriu3.data.converters

import androidx.room.TypeConverter
import com.fk.thewitcheriu3.data.dto.GameState
import kotlinx.serialization.json.Json

class GameStateConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromGameState(gameState: GameState): String {
        return json.encodeToString(gameState)
    }

    @TypeConverter
    fun toGameState(gameStateString: String): GameState {
        return json.decodeFromString(gameStateString)
    }
} 