package com.fk.thewitcheriu3.domain.models

sealed class NavRoutes(val route: String) {
    data object MainMenu : NavRoutes("main menu")
    data object NewGame : NavRoutes("new game")
    data object MapCreator : NavRoutes("map creator")
    data object Settings : NavRoutes("settings")
    data object SaveLoadMenu : NavRoutes("save load menu")
}