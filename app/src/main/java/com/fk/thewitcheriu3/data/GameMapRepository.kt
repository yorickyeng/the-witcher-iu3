package com.fk.thewitcheriu3.data

import com.fk.thewitcheriu3.domain.models.GameMap
import kotlinx.coroutines.flow.Flow

interface GameMapRepository {
    suspend fun saveGame(gameMap: GameMap, saveName: String)
    suspend fun loadGame(id: Int): GameMap?
    suspend fun getAllSaves(): List<Pair<Int, String>>
    suspend fun deleteSave(id: Int)
    suspend fun deleteAllSaves()
    fun getHighScores(): Flow<List<Pair<String, Int>>>
}