package com.fk.thewitcheriu3.domain.models

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.models.characters.units.Unit

data class Cell(
    val type: String,
    val hero: Hero? = null,
    val unit: Unit? = null,
    val xCoord: Int,
    val yCoord: Int,
) {
    val texture = when (type) {
        "road" -> R.drawable.road
        "field" -> R.drawable.field
        "forest" -> R.drawable.forest
        "tavern" -> R.drawable.tavern
        "Kaer Morhen" -> R.drawable.kaer_morhen
        "Zamek Stygga" -> R.drawable.stygga
        else -> R.drawable.field
    }
}