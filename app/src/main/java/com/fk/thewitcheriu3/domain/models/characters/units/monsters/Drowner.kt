package com.fk.thewitcheriu3.domain.models.characters.units.monsters

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.characters.units.Monster

class Drowner : Monster(
    type = "Drowner", health = 50, damage = 50, attackRange = 3, price = 50
) {
    override val texture = R.drawable.drowner
}
