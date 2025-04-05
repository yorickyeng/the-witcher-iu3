package com.fk.thewitcheriu3.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fk.thewitcheriu3.data.dao.GameDao
import com.fk.thewitcheriu3.data.entities.GameMapEntity

@Database(entities = [GameMapEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE gameMap ADD COLUMN movesCounter INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}