package com.fk.thewitcheriu3.domain.models.gwent

import androidx.annotation.DrawableRes
import com.fk.thewitcheriu3.R

enum class GwentCardRow {
    MELEE,
    RANGED,
    SIEGE,
    WEATHER
}

data class GwentCard(
    val id: Int,
    val name: String,
    val power: Int,
    val row: GwentCardRow,
    @DrawableRes val imageRes: Int,
    val description: String = "",
    val isHero: Boolean = false,
    val weatherEffect: WeatherEffect? = null
)

enum class WeatherEffect {
    FROST,  // Обнуляет силу карт ближнего боя
    FOG,    // Обнуляет силу карт дальнего боя
    RAIN    // Обнуляет силу осадных орудий
} 