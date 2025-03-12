package com.fk.thewitcheriu3.domain

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Computer
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Player
import com.fk.thewitcheriu3.domain.entities.characters.units.Witcher

fun selectedCharacterActions(
    selectedCharacter: Character,
    cell: Cell,
    gameMap: GameMap,
    player: Player,
    computer: Computer,
    movesCounter: MutableIntState,
    gameOver: MutableState<String?>,
    showBuyMenu: MutableState<Boolean>
) {
    val (selectedX, selectedY) = selectedCharacter.getPosition()
    val distance =
        measureDistance(
            fromX = selectedX,
            fromY = selectedY,
            toX = cell.xCoord,
            toY = cell.yCoord,
            cell = cell,
            character = selectedCharacter
        )

    // Если клетка в пределах ходьбы
    if (distance <= selectedCharacter.moveRange &&
        (selectedCharacter is Player || selectedCharacter is Witcher)
    ) {
        when {
            // Если это мой замок
            cell.xCoord == 0 && cell.yCoord == 0 -> {
                showBuyMenu.value = true // Показываем меню покупки
            }
            // Если это вражеский замок
            cell.xCoord == gameMap.width - 1 && cell.yCoord == gameMap.height - 1 -> {
                computer.health = 0
                checkGameOver(player, computer, gameOver)
            }
            // Если клетка пуста, перемещаем персонажа
            cell.hero == null && cell.unit == null -> {
                selectedCharacter.move(gameMap, cell.xCoord, cell.yCoord)
                movesCounter.intValue += 1


            }
        }
    }

    // Если клетка занята и цель в пределах атаки
    if (distance <= selectedCharacter.attackRange &&
        (selectedCharacter is Player || selectedCharacter is Witcher)
    ) {
        val target = cell.hero ?: cell.unit
        if (target != null) {
            selectedCharacter.attack(gameMap, target, gameOver)
            movesCounter.intValue += 1
        }
    }

    if (movesCounter.intValue == player.units.size + 1) {
        computersTurn(gameMap, player, computer, gameOver)
        movesCounter.intValue = 0
    }
}