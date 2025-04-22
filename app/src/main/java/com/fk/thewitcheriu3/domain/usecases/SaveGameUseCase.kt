package com.fk.thewitcheriu3.domain.usecases

import com.fk.thewitcheriu3.data.GameMapRepository
import com.fk.thewitcheriu3.domain.models.GameMap

class SaveGameUseCase(private val repository: GameMapRepository) {
    suspend operator fun invoke(gameMap: GameMap) {
//        repository.saveGame(gameMap)
    }
}