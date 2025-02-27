package com.fk.thewitcheriu3.domain.entities.heroes

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.entities.GameMap

class Player(
    name: String, xCoord: Int, yCoord: Int, gameMap: GameMap
) : Hero(
    name, xCoord, yCoord, gameMap
) {
    override val texture = R.drawable.ciri
}