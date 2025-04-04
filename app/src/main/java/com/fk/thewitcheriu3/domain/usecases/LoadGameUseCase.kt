package com.fk.thewitcheriu3.domain.usecases

import com.fk.thewitcheriu3.data.GameMapRepository
import com.fk.thewitcheriu3.domain.models.GameMap

class LoadGameUseCase(private val repository: GameMapRepository) {
    suspend operator fun invoke(id: Int) {
//        return repository.loadGame(id)
    }
}