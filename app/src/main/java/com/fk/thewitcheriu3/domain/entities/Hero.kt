package com.fk.thewitcheriu3.domain.entities

import com.fk.thewitcheriu3.R

abstract class Hero protected constructor(
    private val name: String, override var xCoord: Int, override var yCoord: Int, gameMap: GameMap
) : Character {

    override var health = 300
    override val damage = 100
    override val moveRange = 4
    override val attackRange = 2
    abstract override val texture: Int

    val units: MutableList<Unit> = mutableListOf()

    fun getName() = name

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

    override fun move(gameMap: GameMap, x: Int, y: Int) {
        super.move(gameMap, xCoord, yCoord)
        gameMap.updateCell(
            Cell(
                type = gameMap.map[y][x].type, hero = this, unit = null, xCoord = x, yCoord = y
            )
        )
        xCoord = x
        yCoord = y

    }

    override fun toString(): String {
        return "$name\nHealth $health\n" + "Damage $damage\nMove Range $moveRange\n" +
                "Attack Range $attackRange\n${units.size} units\nMoney $money"
    }

    fun addUnit(unit: Unit) {
        units.add(unit)
    }

    private var _money: Int = 200
    var money: Int
        set(value) {
            _money = value
        }
        get() = _money
}

class Player(
    name: String, xCoord: Int, yCoord: Int, gameMap: GameMap
) : Hero(
    name, xCoord, yCoord, gameMap
) {
    override val texture = R.drawable.ciri
}

class Computer(
    name: String, xCoord: Int, yCoord: Int, gameMap: GameMap
) : Hero(
    name, xCoord, yCoord, gameMap
) {
    override val texture = R.drawable.vilgefortz
}