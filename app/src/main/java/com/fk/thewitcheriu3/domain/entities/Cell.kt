package com.fk.thewitcheriu3.domain.entities

import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.characters.units.Unit
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Cell(
    val type: String,
    @Transient val hero: Hero? = null,
    @Transient val unit: Unit? = null,
    val xCoord: Int,
    val yCoord: Int
) {
    val texture = when (type) {
        "road" -> R.drawable.road
        "field" -> R.drawable.field
        "forest" -> R.drawable.forest
        "Kaer Morhen" -> R.drawable.kaer_morhen
        "Zamek Stygga" -> R.drawable.stygga
        else -> R.drawable.field
    }
}