package com.fk.thewitcheriu3.domain.entities.units.monsters

import com.fk.thewitcheriu3.domain.entities.units.Monster

abstract class Vampire(
    type: String = "Vampire",
    health: Int,
    damage: Int,
    moveRange: Int = 2,
    attackRange: Int,
    price: Int
) : Monster(
    type, health, damage, moveRange, attackRange, price
)