package com.fk.thewitcheriu3.domain.entities.units

import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.getRandomCoords

abstract class Witcher(
    type: String = "Witcher",
    health: Int,
    damage: Int,
    moveRange: Int = 3,
    attackRange: Int,
    price: Int
) : Unit(
    type, health, damage, moveRange, attackRange, price
) {
    final override var xCoord: Int = 1
    final override var yCoord: Int = 1

    final override fun place(gameMap: GameMap) {
        val (xRandom, yRandom) = getRandomCoords(
            gameMap, 0, 4, 0, 4
        )
        xCoord = xRandom
        yCoord = yRandom
        super.place(gameMap)
    }
}