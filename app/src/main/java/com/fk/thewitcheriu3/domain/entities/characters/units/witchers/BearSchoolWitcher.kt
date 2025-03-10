package com.fk.thewitcheriu3.domain.entities.characters.units.witchers

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.entities.characters.units.Witcher

class BearSchoolWitcher : Witcher(
    type = "Bear School Witcher", health = 200, damage = 200, attackRange = 2, price = 200
) {
    override val texture = R.drawable.bear
}