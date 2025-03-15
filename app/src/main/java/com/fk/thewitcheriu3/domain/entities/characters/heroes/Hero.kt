package com.fk.thewitcheriu3.domain.entities.characters.heroes

import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.units.Unit

abstract class Hero protected constructor(
    private val name: String, override var xCoord: Int, override var yCoord: Int, gameMap: GameMap
) : Character {

    override var health = 300
    override val damage = 100
    override val moveRange = 4
    override val attackRange = 2
    abstract override val texture: Int

    private var _money: Int = 200
    var money: Int
        set(value) {
            _money = value
        }
        get() = _money

    val units: MutableList<Unit> = mutableListOf()

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

    fun buy(gameMap: GameMap, unit: Unit): Boolean {
        val price = unit.getPrice()
        return if (money >= price) {
            money -= price
            unit.place(gameMap)
            addUnit(unit)
            true
        } else false
    }

    override fun toString() =
        "$name\nHealth $health\n" + "Damage $damage\nMove Range $moveRange\n" +
                "Attack Range $attackRange\n${units.size} units\nMoney $money"

    fun addUnit(unit: Unit) {
        units.add(unit)
    }

    fun getName() = name
}