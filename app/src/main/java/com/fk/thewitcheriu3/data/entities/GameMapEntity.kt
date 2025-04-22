package com.fk.thewitcheriu3.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fk.thewitcheriu3.data.converters.GameStateConverter
import com.fk.thewitcheriu3.data.dto.GameState

@Entity(tableName = "gameMap")
@TypeConverters(GameStateConverter::class)
data class GameMapEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gameMapId")
    var id: Int = 0,

    @ColumnInfo(name = "gameState")
    var gameState: GameState,

    @ColumnInfo(name = "saveName")
    var saveName: String = "",

    @ColumnInfo(name = "playerMoney")
    var playerMoney: Int,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "movesCounter")
    var movesCounter: Int = 0
)