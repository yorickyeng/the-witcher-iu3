package com.fk.thewitcheriu3.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fk.thewitcheriu3.data.dao.GameDao
import com.fk.thewitcheriu3.data.entities.GameMapEntity

@Database(entities = [GameMapEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}