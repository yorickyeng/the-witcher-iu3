package com.fk.thewitcheriu3.domain.models.characters.units

import com.fk.thewitcheriu3.domain.models.Cell
import com.fk.thewitcheriu3.domain.models.characters.Character
import com.fk.thewitcheriu3.domain.models.GameMap

abstract class Unit protected constructor(
    private val type: String,
    override var health: Int,
    override val damage: Int,
    override val moveRange: Int,
    override val attackRange: Int,
    private val price: Int
) : Character {

    abstract override var xCoord: Int
    abstract override var yCoord: Int
    abstract override val texture: Int

    fun getType() = type
    fun getPrice() = price

    override fun place(gameMap: GameMap) {
        gameMap.updateCell(
            Cell(
                type = gameMap.map[yCoord][xCoord].type,
                unit = this,
                xCoord = xCoord,
                yCoord = yCoord
            )
        )
    }

    override fun move(gameMap: GameMap, x: Int, y: Int) {
        super.move(gameMap, xCoord, yCoord)
        gameMap.updateCell(
            Cell(
                type = gameMap.map[y][x].type, hero = null, unit = this, xCoord = x, yCoord = y
            )
        )
        xCoord = x
        yCoord = y
    }

    override fun toString() =
        "\n$type\nHealth $health\nDamage $damage\nMove Range $moveRange\nAttack Range $attackRange"
}