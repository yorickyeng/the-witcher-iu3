package com.fk.thewitcheriu3.domain.models.characters.units.witchers

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.characters.units.Witcher

class CatSchoolWitcher : Witcher(
    type = "CatSchoolWitcher",
    health = 75,
    damage = 100,
    moveRange = 5,
    attackRange = 3,
    price = 100,
) {
    override val texture = R.drawable.gaetan
}