package com.fk.thewitcheriu3.domain

import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.units.Unit
import com.fk.thewitcheriu3.domain.entities.units.monsters.Bruxa
import com.fk.thewitcheriu3.domain.entities.units.monsters.Drowner
import com.fk.thewitcheriu3.domain.entities.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.GWENTWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.WolfSchoolWitcher

fun buyUnit(gameMap: GameMap, hero: Hero, unitType: String): Boolean {
    val units = listOf(
        CatSchoolWitcher(),
        WolfSchoolWitcher(),
        BearSchoolWitcher(),
        GWENTWitcher(),
        Drowner(),
        Bruxa()
    )

    for (unit in units) {
        return when (unitType) {
            unit.getType() -> {
                buy(hero, unit, gameMap)
            }
            else -> false
        }
    }

    return false
}

fun buy(hero: Hero, unit: Unit, gameMap: GameMap): Boolean {
    return if (hero.money >= unit.getPrice()) {
        hero.money -= unit.getPrice()
        unit.place(gameMap)
        hero.addUnit(unit)
        true
    } else false
}