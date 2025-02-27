package com.fk.thewitcheriu3.domain.entities.units

import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.getRandomCoords

abstract class Monster(
    type: String = "Monster",
    health: Int,
    damage: Int,
    moveRange: Int = 4,
    attackRange: Int,
    price: Int
) : Unit(
    type, health, damage, moveRange, attackRange, price
) {
    final override var xCoord: Int = 8
    final override var yCoord: Int = 8

    final override fun place(gameMap: GameMap) {
        val (xRandom, yRandom) = getRandomCoords(
            gameMap, gameMap.width - 4, gameMap.width, gameMap.height - 4, gameMap.height
        )
        xCoord = xRandom
        yCoord = yRandom
        super.place(gameMap)
    }
}