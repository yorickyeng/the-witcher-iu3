package com.fk.thewitcheriu3.domain.models.characters.heroes

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.GameMap

class Computer(
    name: String, xCoord: Int, yCoord: Int, gameMap: GameMap
) : Hero(
    name, xCoord, yCoord, gameMap
) {
    override val texture = R.drawable.vilgefortz
}