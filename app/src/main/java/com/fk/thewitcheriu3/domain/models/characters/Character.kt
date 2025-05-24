package com.fk.thewitcheriu3.domain.models.characters

import com.fk.thewitcheriu3.domain.models.Cell
import com.fk.thewitcheriu3.domain.models.GameMap

interface Character {
    var health: Int
    var damage: Int
    var moveRange: Int
    var attackRange: Int
    val xCoord: Int
    val yCoord: Int
    val texture: Int

    fun getName(): String

    fun getPosition() = Pair(xCoord, yCoord)

    fun place(gameMap: GameMap)

    fun move(gameMap: GameMap, x: Int, y: Int) {
        gameMap.updateCell(
            Cell(
                type = gameMap.map[y][x].type, hero = null, unit = null, xCoord = x, yCoord = y
            )
        )
    }

    fun attack(target: Character) {
        target.health -= damage
    }

    override fun toString(): String
}