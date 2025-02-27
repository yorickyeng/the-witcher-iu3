package com.fk.thewitcheriu3.domain.entities

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.entities.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.units.Unit

data class Cell(
    val type: String,
    val hero: Hero? = null,
    val unit: Unit? = null,
    val xCoord: Int,
    val yCoord: Int
) {
    val texture = when (type) {
        "road" -> R.drawable.road
        "field" -> R.drawable.field
        "forest" -> R.drawable.forest
        "castle" -> when (Pair(xCoord, yCoord)) {
            Pair(0, 0) -> R.drawable.kaer_morhen
            Pair(9, 9) -> R.drawable.stygga
            else -> R.drawable.field
        }

        else -> R.drawable.field
    }
}