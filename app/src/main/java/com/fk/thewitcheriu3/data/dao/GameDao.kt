package com.fk.thewitcheriu3.data.dao

import androidx.room.*
import com.fk.thewitcheriu3.data.entities.GameMapEntity

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameMap: GameMapEntity)

    @Query("SELECT * FROM gameMap ORDER BY timestamp DESC")
    suspend fun getAllSaves(): List<GameMapEntity>

    @Query("SELECT * FROM gameMap WHERE gameMapId = :id")
    suspend fun getSaveById(id: Int): GameMapEntity?

    @Query("DELETE FROM gameMap WHERE gameMapId = :id")
    suspend fun deleteSave(id: Int)

    @Query("DELETE FROM gameMap")
    suspend fun deleteAllSaves()
}