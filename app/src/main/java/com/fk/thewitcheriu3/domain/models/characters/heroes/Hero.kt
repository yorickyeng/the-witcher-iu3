package com.fk.thewitcheriu3.domain.models.characters.heroes

import com.fk.thewitcheriu3.domain.models.Cell
import com.fk.thewitcheriu3.domain.models.characters.Character
import com.fk.thewitcheriu3.domain.models.GameMap
import com.fk.thewitcheriu3.domain.models.characters.units.Unit

abstract class Hero protected constructor(
    internal val name: String, override var xCoord: Int, override var yCoord: Int, gameMap: GameMap
) : Character {

    override var health = 300
    override var damage = 100
    override var moveRange = 4
    override var attackRange = 2
    abstract override val texture: Int

    var money: Int = 200
    internal val units: MutableList<Unit> = mutableListOf()

    init {
        place(gameMap)
    }

    final override fun place(gameMap: GameMap) {
        gameMap.updateCell(
            Cell(
                type = gameMap.map[yCoord][xCoord].type,
                hero = this,
                xCoord = xCoord,
                yCoord = yCoord
            )
        )
    }

    final override fun move(gameMap: GameMap, x: Int, y: Int) {
        super.move(gameMap, xCoord, yCoord)
        gameMap.updateCell(
            Cell(
                type = gameMap.map[y][x].type, hero = this, unit = null, xCoord = x, yCoord = y
            )
        )
        xCoord = x
        yCoord = y
    }


    override fun toString() =
        "$name\nHealth $health\n" + "Damage $damage\nMove Range $moveRange\n" + "Attack Range $attackRange\n${units.size} units\nMoney $money"

    fun addUnit(unit: Unit) {
        units.add(unit)
    }

    override fun getName() = name
}