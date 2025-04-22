package com.fk.thewitcheriu3.domain.models.characters.units.monsters

import com.fk.thewitcheriu3.R

class Bruxa : Vampire(
    type = "Bruxa", health = 150, damage = 100, attackRange = 2, price = 200
) {
    override val texture = R.drawable.bruxa
}