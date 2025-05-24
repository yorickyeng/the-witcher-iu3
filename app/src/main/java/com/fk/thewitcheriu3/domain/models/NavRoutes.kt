package com.fk.thewitcheriu3.domain.models

import com.fk.thewitcheriu3.R

sealed class NavRoutes(val route: String, val musicResId: Int? = null) {
    object MainMenu : NavRoutes("main_menu", R.raw.kaer_morhen)
    object NewGame : NavRoutes("new_game", R.raw.new_game_theme)
    object MapCreator : NavRoutes("map_creator", null)
    object Settings : NavRoutes("settings", null)
    object SaveLoadMenu : NavRoutes("save_load", null)
    object Records : NavRoutes("records", null)
    object Gwent : NavRoutes("gwent", R.raw.gwent_theme)
}