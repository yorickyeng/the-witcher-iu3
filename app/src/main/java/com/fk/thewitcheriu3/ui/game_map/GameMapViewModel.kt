package com.fk.thewitcheriu3.ui.game_map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Computer
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Player
import com.fk.thewitcheriu3.domain.entities.characters.units.Monster
import com.fk.thewitcheriu3.domain.entities.characters.units.Unit
import com.fk.thewitcheriu3.domain.entities.characters.units.Witcher
import com.fk.thewitcheriu3.domain.measureDistance
import kotlinx.coroutines.delay
import kotlin.random.Random

class GameMapViewModel: ViewModel() {

    var gameMap by mutableStateOf(GameMap(10, 10))
        private set

    var player by mutableStateOf(gameMap.getPlayer())
        private set

    var selectedCell = mutableStateOf<Pair<Int, Int>?>(null)
        private set

    var cellsInMoveRange = mutableStateOf<Set<Pair<Int, Int>>>(emptySet())
        private set

    var cellsInAttackRange = mutableStateOf<Set<Pair<Int, Int>>>(emptySet())
        private set

    var showBuyMenu = mutableStateOf(false)
        private set

    var showRaccoon = mutableStateOf(false)
        private set

    var gameOver = mutableStateOf<String?>(null)
        private set

    private var computer by mutableStateOf(gameMap.getComputer())
    internal var selectedCharacter by mutableStateOf<Character?>(null)
    internal var movesCounter = mutableIntStateOf(0)

    fun handleCellClick(cell: Cell) {
        selectedCell.value = Pair(cell.xCoord, cell.yCoord)

        selectedCharacter?.let { selectedChar ->
            // Если персонаж уже выбран
            // Здесь cell - 2я выбранная клетка (выбор места движения или атаки)

            selectedCharacterMoveAndAttackLogic(selectedChar, targetCell = cell)

            changeTurn()
            resetSelected()

        } ?: run {
            // Если персонаж ещё не выбран
            // Здесь cell - 1я выбранная клетка (выбор союзника)

            when {
                cell.hero is Hero -> selectedCharacter = cell.hero
                cell.unit is Unit -> selectedCharacter = cell.unit
            }

            selectedCharacter?.let {
                cellsInMoveRange.value = gameMap.updateRangeCells(it).first
                cellsInAttackRange.value = gameMap.updateRangeCells(it).second
            }
        }
    }

    fun handleBuyUnit(unitType: String) {
        gameMap.buyUnit(player, unitType)
        showBuyMenu.value = false
    }

    fun refreshGame() {
        gameMap = GameMap(10, 10)
        player = gameMap.getPlayer()
        computer = gameMap.getComputer()

        resetSelected()

        movesCounter.intValue = 0
        showBuyMenu.value = false
        gameOver.value = null
    }

    fun getCellInfo(): String {
        val (x, y) = selectedCell.value!!
        val cell = gameMap.map[y][x]
        return when {
            cell.hero != null -> cell.hero.toString()
            cell.unit != null -> cell.unit.toString()
            else -> cell.type
        }
    }

    private fun resetSelected() {
        selectedCharacter = null
        selectedCell.value = null
        cellsInMoveRange.value = emptySet()
        cellsInAttackRange.value = emptySet()
    }

    internal fun selectedCharacterMoveAndAttackLogic(selectedChar: Character, targetCell: Cell) {
        val (selectedX, selectedY) = selectedChar.getPosition()
        val distance = measureDistance(
            fromX = selectedX,
            fromY = selectedY,
            toX = targetCell.xCoord,
            toY = targetCell.yCoord,
            targetCell = targetCell,
            character = selectedChar
        )

        if (selectedChar is Player || selectedChar is Witcher) {
            if (targetCell.hero == null && targetCell.unit == null) {
                if (distance <= selectedChar.moveRange) {
                    allyMoves(selectedChar, targetCell)
                }
            } else {
                val target = (targetCell.hero ?: targetCell.unit)!!
                if (distance <= selectedChar.attackRange) {
                    allyAttacks(selectedChar, target)
                }
            }
        }
    }

    private fun changeTurn() {
        if (movesCounter.intValue == player.units.size + 1) {
            computersTurn()
            movesCounter.intValue = 0
        }
    }

    private fun computersTurn() {
        enemyMoves()
        enemyAttacks()

        for (unit in computer.units) {
            enemyMoves(unit)
            enemyAttacks(unit)
        }

        val monsterTypes = arrayListOf("Drowner", "Bruxa")
        gameMap.buyUnit(computer, monsterTypes.random())
    }

    private fun enemyMoves(enemy: Character = computer) {
        val (distance, xRandom, yRandom) = gameMap.findCoordsAndDistanceForEnemy(enemy)
        if (distance <= enemy.moveRange) {
            enemy.move(gameMap, xRandom, yRandom)

            if (xRandom == 0 && yRandom == 0) {
                player.health = 0
                gameOver.value = gameMap.checkGameOver()
            }
        }
    }

    private fun enemyAttacks(enemy: Character = computer) {
        val target = gameMap.findAttackTargetForEnemy(enemy)
        if (target != null) {
            gameOver.value = gameMap.characterAttacks(enemy, target)
        }
    }

    internal fun allyMoves(ally: Character, targetCell: Cell) {
        ally.move(gameMap, targetCell.xCoord, targetCell.yCoord)
        movesCounter.intValue += 1

        when (targetCell.type) {
            // Если это мой замок
            "Kaer Morhen" -> showBuyMenu.value = true

            // Если это вражеский замок
            "Zamek Stygga" -> {
                computer.health = 0
                gameOver.value = gameMap.checkGameOver()
            }
        }
    }

    private fun allyAttacks(ally: Character, target: Character) {
        if (target is Computer || target is Monster) {
            gameOver.value = gameMap.characterAttacks(ally, target)
            movesCounter.intValue += 1
        }
    }

    suspend fun startRaccoonTimer() {
        while (true) {
            // Случайное время появления енота (от 0 до 60 секунд)
            val randomTime = Random.nextLong(0, 60000)
            delay(randomTime)

            // Проверяем, что список умерших не пустой
            if (gameMap.anybodyDied()) {
                showRaccoon.value = true
                Log.i("GameMap", "Raccoon has come!")

                // Случайное количество воскрешений (от 1 до количества умерших)
                val resurrectCount = Random.nextInt(1, gameMap.getDeathNoteSize() + 1)
                repeat(resurrectCount) {
                    gameMap.resurrect()
                    Log.i("GameMap", "Resurrected a character. Total resurrected: $it")
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