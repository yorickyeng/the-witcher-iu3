package com.fk.thewitcheriu3.domain

import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.characters.units.Unit
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Bruxa
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Drowner
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.GWENTWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.WolfSchoolWitcher

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
        if (unit.getType() == unitType) {
            return buy(hero, unit, gameMap)
        }
    }

    return false
}

fun buy(hero: Hero, unit: Unit, gameMap: GameMap): Boolean {
    return if (hero.money >= unit.getPrice()) {
        hero.money -= unit.getPrice()
        unit.place(gameMap)
        hero.addUnit(unit)
        println("${hero.getName()} bought ${unit.getType()} for ${unit.getPrice()} orens")
        true
    } else {
        println("${hero.getName()} not enough money to buy ${unit.getPrice()}")
        false
    }
}