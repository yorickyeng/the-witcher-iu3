package com.fk.thewitcheriu3.domain.models.characters.units.witchers

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.characters.units.Witcher

class GWENTWitcher : Witcher(
    type = "GWENTWitcher",
    health = 10000,
    damage = 10000,
    moveRange = 10,
    attackRange = 100,
    price = 1000
) {
    override val texture = R.drawable.gwent
}