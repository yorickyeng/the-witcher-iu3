package com.fk.thewitcheriu3.ui.game_map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.GameMapCreator

class GameMapCreatorViewModel : ViewModel() {

    var gameMapCreator by mutableStateOf(GameMapCreator(10, 10))
        private set

    var currentMenu by mutableStateOf("main")
        private set

    var selectedType by mutableStateOf("forest")
        private set

    private var castleSelection by mutableStateOf(false)

    fun handleCellClick(cell: Cell) {
        gameMapCreator.updateCell(
            Cell(
                type = selectedType, xCoord = cell.xCoord, yCoord = cell.yCoord
            )
        )
    }

    fun chooseCastle() {
        currentMenu = "castle"
        castleSelection = true
    }

    fun chooseType(type: String) {
        selectedType = type
    }

    fun goBack() {
        currentMenu = "main"
    }
}