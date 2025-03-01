package com.fk.thewitcheriu3.domain

import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Player

fun checkGameOver(player: Player, computer: Character, gameOver: MutableState<String?>) {
    if (player.health <= 0) {
        gameOver.value = "lose" // Поражение
    } else if (computer.health <= 0) {
        gameOver.value = "win" // Победа
    }
}