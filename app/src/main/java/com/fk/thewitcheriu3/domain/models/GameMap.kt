package com.fk.thewitcheriu3.domain.models

import com.fk.thewitcheriu3.domain.models.characters.Character
import com.fk.thewitcheriu3.domain.models.characters.heroes.Computer
import com.fk.thewitcheriu3.domain.models.characters.heroes.Player
import com.fk.thewitcheriu3.domain.models.characters.units.Monster
import com.fk.thewitcheriu3.domain.models.characters.units.Witcher
import com.fk.thewitcheriu3.domain.models.characters.units.monsters.Bruxa
import com.fk.thewitcheriu3.domain.models.characters.units.monsters.Drowner
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.WolfSchoolWitcher
import com.fk.thewitcheriu3.domain.getRandomCoords
import com.fk.thewitcheriu3.domain.measureDistance
import com.fk.thewitcheriu3.domain.models.characters.units.Unit
import kotlin.math.max
import kotlin.math.min

class GameMap(
    val width: Int, val height: Int
) {
    val map: Array<Array<Cell>> = Array(height) { i ->
        Array(width) { j ->
            Cell(type = "field", xCoord = j, yCoord = i)
        }
    }

    private var player = Player(name = "Cirilla Fiona Elen Riannon", 0, 1, this)
    private var computer = Computer(name = "Vilgefortz of Roggeveen", 9, 8, this)

    internal var units: List<Unit> = player.units + computer.units
    internal var movesCounter = player.units.size + 1

    private var deathNote = mutableListOf<Character>()

    fun getPlayer() = player
    fun getComputer() = computer
    fun setPlayer(player: Player) {
        this.player = player
    }
    fun setComputer(computer: Computer) {
        this.computer = computer
    }

    fun updateCell(cell: Cell) {
        map[cell.yCoord][cell.xCoord] = cell
    }

    fun clearCell(cell: Cell) {
        updateCell(
            Cell(
                type = cell.type,
                hero = null,
                unit = null,
                xCoord = cell.xCoord,
                yCoord = cell.yCoord
            )
        )
    }

    fun updateRangeCells(selectedCharacter: Character): Pair<MutableSet<Pair<Int, Int>>, MutableSet<Pair<Int, Int>>> {
        val moveRangeCells = mutableSetOf<Pair<Int, Int>>()
        val attackRangeCells = mutableSetOf<Pair<Int, Int>>()

        for (i in 0 until height) {
            for (j in 0 until width) {
                val distance = measureDistance(
                    fromX = selectedCharacter.xCoord,
                    fromY = selectedCharacter.yCoord,
                    toX = j,
                    toY = i,
                    targetCell = map[i][j],
                    character = selectedCharacter
                )
                if (distance <= selectedCharacter.moveRange) {
                    moveRangeCells.add(Pair(j, i))
                }
                if (distance <= selectedCharacter.attackRange) {
                    attackRangeCells.add(Pair(j, i))
                }
            }
        }

        return Pair(moveRangeCells, attackRangeCells)
    }

    fun checkGameOver(): String? {
        return when {
            player.health <= 0 -> "lose"
            computer.xCoord == 0 && computer.yCoord == 0 -> "lose"
            computer.health <= 0 -> "win"
            player.xCoord == 9 && player.yCoord == 9 -> "win"
            else -> null
        }
    }

    fun findAttackTargetForEnemy(enemy: Character): Character? {
        val (playerX, playerY) = player.getPosition()
        val (computerX, computerY) = enemy.getPosition()

        // Проверяем героя игрока
        val distance = measureDistance(
            fromX = computerX,
            fromY = computerY,
            toX = playerX,
            toY = playerY,
            targetCell = map[playerY][playerX],
            character = enemy
        )
        if (distance <= enemy.attackRange) {
            return player
        }

        // Проверяем юнитов игрока
        for (unit in player.units) {
            val (unitX, unitY) = unit.getPosition()
            val distanceToUnit = measureDistance(
                fromX = computerX,
                fromY = computerY,
                toX = unitX,
                toY = unitY,
                targetCell = map[unitY][unitX],
                character = enemy
            )
            if (distanceToUnit <= enemy.attackRange) {
                return unit
            }
        }

        return null
    }

    fun findCoordsAndDistanceForEnemy(enemy: Character): Triple<Int, Int, Int> {
        val (enemyX, enemyY) = enemy.getPosition()

        val minX = max(0, enemyX - enemy.moveRange)
        val maxX = min(width - 1, enemyX + enemy.moveRange)
        val minY = max(0, enemyY - enemy.moveRange)
        val maxY = min(height - 1, enemyY + enemy.moveRange)

        val (xRandom, yRandom) = getRandomCoords(this, minX, maxX, minY, maxY)
        val targetCell = map[yRandom][xRandom]
        val distance = measureDistance(
            fromX = enemyX,
            fromY = enemyY,
            toX = xRandom,
            toY = yRandom,
            targetCell = targetCell,
            character = enemy
        )
        return Triple(distance, xRandom, yRandom)
    }

    internal fun died(character: Character) {
        deathNote.add(character)
    }

    fun anybodyDied() = deathNote.isNotEmpty()
    fun getDeathNoteSize() = deathNote.size

    fun resurrect() {
        if (anybodyDied()) {
            for (dead in deathNote) {
                dead.health = 100
                dead.place(this)
                if (dead is Monster) {
                    computer.units.add(dead)
                } else if (dead is Witcher) {
                    player.units.add(dead)
                }
            }
            deathNote.clear()
        }
    }

    private fun initDefaultMap() {
        for (i in 0 until width) {
            updateCell(Cell(type = "road", xCoord = i, yCoord = i))
        }

        for (i in 0 until height / 2) {
            for (j in width / 2 until width) {
                if (j >= i + 5) {
                    updateCell(Cell(type = "forest", xCoord = i, yCoord = j))
                }
            }
        }

        for (i in height / 2 until height) {
            for (j in 0 until width) {
                if (j <= i - 5) {
                    updateCell(Cell(type = "forest", xCoord = i, yCoord = j))
                }
            }
        }

        updateCell(Cell(type = "Kaer Morhen", xCoord = 0, yCoord = 0))
        updateCell(Cell(type = "Zamek Stygga", xCoord = width - 1, yCoord = height - 1))

        initCharacters()
    }

    private fun initCharacters() {
        val catSchoolWitcher = CatSchoolWitcher()
        catSchoolWitcher.place(this)
        player.addUnit(catSchoolWitcher)

        val wolfSchoolWitcher = WolfSchoolWitcher()
        wolfSchoolWitcher.place(this)
        player.addUnit(wolfSchoolWitcher)

        for (i in 5 until 8) {
            val drowner = Drowner()
            drowner.place(this)
            computer.addUnit(drowner)
        }

        val bruxa = Bruxa()
        bruxa.place(this)
        computer.addUnit(bruxa)
    }

    init {
        initDefaultMap()
        initCharacters()
    }
}