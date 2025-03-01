package com.fk.thewitcheriu3.domain.entities.characters.units.witchers

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.entities.characters.units.Witcher

class CatSchoolWitcher : Witcher(
    type = "Cat School Witcher",
    health = 75,
    damage = 100,
    moveRange = 5,
    attackRange = 3,
    price = 100
) {
    override val texture = R.drawable.gaetan
}