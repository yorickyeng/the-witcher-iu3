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
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Bruxa
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Drowner
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.GWENTWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.WolfSchoolWitcher
import com.fk.thewitcheriu3.domain.measureDistance
import kotlinx.coroutines.delay
import kotlin.random.Random

class GameMapViewModel : ViewModel() {

    var gameMap by mutableStateOf(GameMap(10, 10))
        private set

    private var player by mutableStateOf(gameMap.getPlayer())
    private var computer by mutableStateOf(gameMap.getComputer())
    internal var selectedCharacter by mutableStateOf<Character?>(null)

    var movesCounter = mutableIntStateOf(0)
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

    var playersMoney by mutableIntStateOf(player.money)
        private set

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

    fun playerBuysUnit(unitType: String) {
        buyUnit(player, unitType)
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

    internal fun allyMoves(ally: Character, targetCell: Cell) {
        when (targetCell.type) {
            // Если это мой замок
            "Kaer Morhen" -> showBuyMenu.value = true

            // Если это вражеский замок
            "Zamek Stygga" -> {
                computer.health = 0
                gameOver.value = gameMap.checkGameOver()
            }
        }

        ally.move(gameMap, targetCell.xCoord, targetCell.yCoord)
        movesCounter.intValue += 1
    }

    private fun allyAttacks(ally: Character, target: Character) {
        if (target is Computer || target is Monster) {
            gameOver.value = characterAttacks(ally, target)
            movesCounter.intValue += 1
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
        buyUnit(computer, monsterTypes.random())
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
            gameOver.value = characterAttacks(enemy, target)
        }
    }

    private fun characterAttacks(character: Character, target: Character): String? {
        character.attack(target)
        if (target.health <= 0) {
            killCharacter(target)
            return gameMap.checkGameOver()
        }
        return null
    }

    private fun killCharacter(target: Character) {
        val cell = gameMap.map[target.yCoord][target.xCoord]
        gameMap.clearCell(cell)
        gameMap.died(target)

        when (target) {
            is Witcher -> {
                player.units.remove(target)
                computer.money += target.getPrice()
            }

            is Monster -> {
                computer.units.remove(target)
                playersMoney += target.getPrice()
            }
        }
    }

    private fun buyUnit(hero: Hero, unitType: String): Boolean {
        val units = listOf(
            CatSchoolWitcher(),
            WolfSchoolWitcher(),
            BearSchoolWitcher(),
            GWENTWitcher(),
            Drowner(),
            Bruxa()
        )

        for (unit in units) {
            if (unit.getType() == unitType) {
                return buy(gameMap, hero, unit)
            }
        }

        return false
    }

    private fun buy(gameMap: GameMap, hero: Hero, unit: Unit): Boolean {
        val price = unit.getPrice()
        return if (hero.money >= price) {
            if (hero is Computer) {
                computer.money -= price
            } else {
                playersMoney -= price
            }
            unit.place(gameMap)
            hero.addUnit(unit)
            true
        } else false
    }

    suspend fun startRaccoonTimer() {
        while (true) {
            val randomTime = Random.nextLong(0, 60000)
            Log.w("Raccoon", "Raccoon is coming in ${randomTime / 1000} seconds!")
            delay(randomTime)

            if (gameMap.anybodyDied()) {
                showRaccoon.value = true
                Log.i("Raccoon", "Raccoon has appeared!")

                val resurrectCount = gameMap.getDeathNoteSize()
                repeat(resurrectCount) {
                    gameMap.resurrect()
                    Log.i(
                        "Raccoon",
                        "Raccoon resurrected a character. Total resurrected: ${it + 1}"
                    )
                }

                delay(7000) // исчезновение енота через 7 секунд
                showRaccoon.value = false
                Log.i(
                    "Raccoon",
                    "Raccoon has disappeared after resurrecting $resurrectCount characters."
                )
            } else {
                Log.e("Raccoon", "Nobody died. Nobody to resurrect.")
            }

            delay(60000) // задержка перед следующим появлением енота 60 секунд
        }
    }
}