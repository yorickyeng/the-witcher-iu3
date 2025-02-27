package com.fk.thewitcheriu3.domain

import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.Character
import com.fk.thewitcheriu3.domain.entities.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.WolfSchoolWitcher
import kotlin.math.abs

fun measureDistance(fromX: Int, fromY: Int, toX: Int, toY: Int, cell: Cell, character: Character) =
    abs(fromX - toX) + abs(fromY - toY) + penalty(cell, character)

fun penalty(cell: Cell, character: Character): Int {
    return when (cell.type) {
        "road" -> -1
        "field" -> 1
        "forest" -> when (character) {
            is CatSchoolWitcher -> 0
            is WolfSchoolWitcher -> 1
            is BearSchoolWitcher -> 2
            else -> 4
        }
        else -> 0
    }
}