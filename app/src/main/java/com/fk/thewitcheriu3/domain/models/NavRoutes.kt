package com.fk.thewitcheriu3.domain.models

sealed class NavRoutes(val route: String) {
    object MainMenu : NavRoutes("mainMenu")
    object NewGame : NavRoutes("newGame")
    object SaveLoadMenu : NavRoutes("saveLoadMenu")
    object Records : NavRoutes("records")
    object MapCreator : NavRoutes("mapCreator")
    object Settings : NavRoutes("settings")
    object Gwent : NavRoutes("gwent")
}