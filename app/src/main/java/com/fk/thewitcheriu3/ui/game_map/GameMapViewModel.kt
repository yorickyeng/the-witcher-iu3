package com.fk.thewitcheriu3.ui.game_map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fk.thewitcheriu3.domain.buyUnit
import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.characters.units.Unit
import com.fk.thewitcheriu3.domain.selectedCharacterActions
import com.fk.thewitcheriu3.domain.updateCellSets
import kotlinx.coroutines.delay
import kotlin.random.Random

class GameMapViewModel : ViewModel() {

    var gameMap by mutableStateOf(GameMap(10, 10))
        private set

    var player by mutableStateOf(gameMap.getPlayer())
        private set

    private var computer by mutableStateOf(gameMap.getComputer())

    var selectedCell = mutableStateOf<Pair<Int, Int>?>(null)
        private set

    private var selectedCharacter by mutableStateOf<Character?>(null)

    var cellsInMoveRange = mutableStateOf<Set<Pair<Int, Int>>>(emptySet())
        private set

    var cellsInAttackRange = mutableStateOf<Set<Pair<Int, Int>>>(emptySet())
        private set

    private var movesCounter = mutableIntStateOf(0)

    var showBuyMenu = mutableStateOf(false)
        private set

    var gameOver = mutableStateOf<String?>(null)
        private set

    var showRaccoon = mutableStateOf(false)
        private set

    fun handleBuyUnit(unitType: String) {
        buyUnit(gameMap, player, unitType)
        showBuyMenu.value = false
    }

    fun handleCellClick(cell: Cell) {
        selectedCell.value = Pair(cell.xCoord, cell.yCoord)

        selectedCharacter?.let { selectedChar ->
            // Если персонаж уже выбран
            selectedCharacterActions(
                selectedChar, cell, gameMap, player, computer, movesCounter, gameOver, showBuyMenu
            )

            selectedCharacter = null // Сброс выбранного персонажа
            selectedCell.value = null // Сброс выбранной клетки
            cellsInMoveRange.value = emptySet() // Сброс выделения moveRange
            cellsInAttackRange.value = emptySet() // Сброс выделения attackRange
        } ?: run {
            // Если персонаж ещё не выбран
            when {
                cell.hero is Hero -> selectedCharacter = cell.hero
                cell.unit is Unit -> selectedCharacter = cell.unit
            }

            // Обновляем клетки в пределах moveRange и attackRange
            selectedCharacter?.let { char ->
                updateCellSets(char, gameMap, cellsInMoveRange, cellsInAttackRange)
            }
        }
    }

    fun refreshGame() {
        gameMap = GameMap(10, 10)
        player = gameMap.getPlayer()
        computer = gameMap.getComputer()

        selectedCell.value = null
        selectedCharacter = null
        cellsInMoveRange.value = emptySet()
        cellsInAttackRange.value = emptySet()

        movesCounter.intValue = 0
        showBuyMenu.value = false
        gameOver.value = null
    }

    suspend fun startRaccoonTimer() {
        while (true) {
            // Случайное время появления енота (от 0 до 60 секунд)
            val randomTime = Random.nextLong(0, 60000)
            delay(randomTime)

            // Проверяем, что список умерших не пустой
            if (gameMap.anybodyDied()) {
                showRaccoon.value = true

                // Случайное количество воскрешений (от 1 до количества умерших)
                val resurrectCount = Random.nextInt(1, gameMap.getDeathNoteSize() + 1)
                repeat(resurrectCount) {
                    gameMap.resurrect()
                }

                // Исчезновение енота через 7 секунд
                delay(7000)
                showRaccoon.value = false
            }

            // Задержка перед следующим появлением енота (60 секунд)
            delay(60000)
        }
    }
}