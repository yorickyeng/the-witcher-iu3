package com.fk.thewitcheriu3.domain.entities

import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Computer
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Player
import com.fk.thewitcheriu3.domain.entities.characters.units.Monster
import com.fk.thewitcheriu3.domain.entities.characters.units.Witcher
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Bruxa
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Drowner
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.GWENTWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.WolfSchoolWitcher
import com.fk.thewitcheriu3.domain.getRandomCoords
import com.fk.thewitcheriu3.domain.measureDistance
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

    private var Ciri: Player
    private var Vilgefortz: Computer

    private var deathNote = mutableListOf<Character>()

    fun getPlayer() = Ciri
    fun getComputer() = Vilgefortz

    fun updateCell(cell: Cell) {
        map[cell.yCoord][cell.xCoord] = cell
    }

    private fun clearCell(cell: Cell) {
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

    fun buyUnit(hero: Hero, unitType: String): Boolean {
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
                return hero.buy(this, unit)
            }
        }

        return false
    }

    fun checkGameOver(): String? {
        return when {
            Ciri.health <= 0 -> "lose"
            Vilgefortz.health <= 0 -> "win"
            else -> null
        }
    }

    fun characterAttacks(character: Character, target: Character): String? {
        character.attack(target)
        if (target.health <= 0) {
            killCharacter(target)
            return checkGameOver()
        }
        return null
    }

    private fun killCharacter(target: Character) {
        val cell = map[target.yCoord][target.xCoord]
        clearCell(cell)
        died(target)

        when (target) {
            is Witcher -> {
                Ciri.units.remove(target)
                Vilgefortz.money += target.getPrice()
            }

            is Monster -> {
                Vilgefortz.units.remove(target)
                Ciri.money += target.getPrice()
            }
        }
    }

    fun findAttackTargetForEnemy(enemy: Character): Character? {
        val (playerX, playerY) = Ciri.getPosition()
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
            return Ciri
        }

        // Проверяем юнитов игрока
        for (unit in Ciri.units) {
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

    private fun died(character: Character) {
        deathNote.add(character)
    }

    fun anybodyDied() = deathNote.isNotEmpty()
    fun getDeathNoteSize() = deathNote.size

    fun resurrect() {
        if (anybodyDied()) {
            for (dead in deathNote) {
                dead.health = 100
                dead.place(this)
            }
            deathNote.clear()
        }
    }

    init { // map initialization
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

        Ciri = Player(name = "Cirilla Fiona Elen Riannon", 0, 1, this)
        Vilgefortz = Computer(name = "Vilgefortz of Roggeveen", 9, 8, this)

        val Gaetan = CatSchoolWitcher()
        Gaetan.place(this)
        Ciri.addUnit(Gaetan)

        val Geralt = WolfSchoolWitcher()
        Geralt.place(this)
        Ciri.addUnit(Geralt)

        for (i in 5 until 8) {
            val drowner = Drowner()
            drowner.place(this)
            Vilgefortz.addUnit(drowner)
        }

        val bruxa = Bruxa()
        bruxa.place(this)
        Vilgefortz.addUnit(bruxa)
    }
}