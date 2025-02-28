package com.fk.thewitcheriu3.domain

import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.entities.characters.units.Monster
import com.fk.thewitcheriu3.domain.entities.characters.units.Witcher

fun killCharacter(gameMap: GameMap, target: Character, gameOver: MutableState<String?>) {
    val cell = gameMap.map[target.yCoord][target.xCoord]
    val player = gameMap.getPlayer()
    val computer = gameMap.getComputer()

    gameMap.clearCell(cell)
    gameMap.died(target)

    when (target) {
        is Hero -> {
            checkGameOver(player, computer, gameOver)
        }

        is Witcher -> {
            player.units.remove(target)
            computer.money += target.getPrice()
        }

        is Monster -> {
            computer.units.remove(target)
            player.money += target.getPrice()
        }
    }
}