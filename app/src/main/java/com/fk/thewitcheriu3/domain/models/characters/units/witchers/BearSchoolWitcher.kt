package com.fk.thewitcheriu3.domain.models.characters.units.witchers

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.characters.units.Witcher

class BearSchoolWitcher : Witcher(
    type = "BearSchoolWitcher", health = 200, damage = 200, attackRange = 2, price = 200
) {
    override val texture = R.drawable.bear
}