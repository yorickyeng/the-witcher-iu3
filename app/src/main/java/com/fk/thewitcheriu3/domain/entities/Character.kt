package com.fk.thewitcheriu3.domain.entities

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
                type = gameMap.map[y][x].type,
                hero = null,
                unit = null,
                xCoord = x,
                yCoord = y
            )
        ) // clear old cell
    }

    fun attack(target: Character) {
        target.health -= damage
    }

    override fun toString(): String
}