package com.fk.thewitcheriu3.domain

import com.fk.thewitcheriu3.domain.entities.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.Bruxa
import com.fk.thewitcheriu3.domain.entities.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.Drowner
import com.fk.thewitcheriu3.domain.entities.GWENTWitcher
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.Hero
import com.fk.thewitcheriu3.domain.entities.Unit
import com.fk.thewitcheriu3.domain.entities.WolfSchoolWitcher

// Функция для покупки юнита
fun buyUnit(gameMap: GameMap, hero: Hero, unitType: String): Boolean {
    return when (unitType) {
        "Cat School Witcher" -> {
            val witcher = CatSchoolWitcher()
            buy(hero = hero, unit = witcher, gameMap)
        }

        "Wolf School Witcher" -> {
            val witcher = WolfSchoolWitcher()
            buy(hero = hero, unit = witcher, gameMap)
        }

        "Bear School Witcher" -> {
            val witcher = BearSchoolWitcher()
            buy(hero = hero, unit = witcher, gameMap)
        }

        "GWENT Witcher" -> {
            val witcher = GWENTWitcher()
            buy(hero = hero, unit = witcher, gameMap)
        }

        "Bruxa" -> {
            val monster = Bruxa()
            buy(hero = hero, unit = monster, gameMap)
        }

        "Drowner" -> {
            val monster = Drowner()
            buy(hero = hero, unit = monster, gameMap)
        }

        else -> false
    }
}

fun buy(hero: Hero, unit: Unit, gameMap: GameMap): Boolean {
    return if (hero.money >= unit.getPrice()) {
        hero.money -= unit.getPrice()
        unit.place(gameMap)
        hero.addUnit(unit)
        true
    } else false
}