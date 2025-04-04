package com.fk.thewitcheriu3.domain.models.characters.units.witchers

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.characters.units.Witcher

class WolfSchoolWitcher : Witcher(
    type = "WolfSchoolWitcher", health = 100, damage = 150, attackRange = 3, price = 150
) {
    override val texture = R.drawable.geralt
}