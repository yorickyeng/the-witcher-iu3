package com.fk.thewitcheriu3.domain.entities.characters

import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.killCharacter

interface Character {
    var health: Int
    val damage: Int
    val moveRange: Int
    val attackRange: Int
    val xCoord: Int
    val yCoord: Int
    val texture: Int

    fun getPosition() = Pair(xCoord, yCoord)

    fun place(gameMap: GameMap)

    fun move(gameMap: GameMap, x: Int, y: Int) {
        gameMap.updateCell(
            Cell(
                type = gameMap.map[y][x].type, hero = null, unit = null, xCoord = x, yCoord = y
            )
        )
    }

    fun attack(gameMap: GameMap, target: Character, gameOver: MutableState<String?>) {
        target.health -= damage

        if (target.health <= 0) {
            killCharacter(gameMap, target, gameOver)
        }
    }

    override fun toString(): String
}