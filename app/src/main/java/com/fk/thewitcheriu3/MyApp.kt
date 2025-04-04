package com.fk.thewitcheriu3

import android.app.Application
import androidx.room.Room
import com.fk.thewitcheriu3.data.AppDatabase
import com.fk.thewitcheriu3.data.GameMapRepositoryImpl

class MyApp: Application() {
    val appDatabase by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = AppDatabase::class.java,
            name = "game-database"
        ).build()
    }

    val gameRepository by lazy {
        GameMapRepositoryImpl(appDatabase.gameDao())
    }
}