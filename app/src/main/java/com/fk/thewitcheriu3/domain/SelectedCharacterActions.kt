package com.fk.thewitcheriu3.domain

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.Character
import com.fk.thewitcheriu3.domain.entities.heroes.Computer
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.units.Monster
import com.fk.thewitcheriu3.domain.entities.heroes.Player
import com.fk.thewitcheriu3.domain.entities.units.Witcher

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

                if (movesCounter.intValue == player.units.size + 1) {
                    computersTurn(gameMap, player, computer, gameOver)
                    movesCounter.intValue = 0
                }
            }
        }
    }

    // Если клетка занята и цель в пределах атаки
    if (distance <= selectedCharacter.attackRange &&
        (selectedCharacter is Player || selectedCharacter is Witcher)
    ) {
        when {
            cell.hero is Computer -> {
                selectedCharacter.attack(cell.hero)
                if (cell.hero.health <= 0) {
                    gameMap.clearCell(cell)
                    checkGameOver(player, computer, gameOver)
                }
            }

            cell.unit is Monster -> {
                selectedCharacter.attack(cell.unit)
                if (cell.unit.health <= 0) {
                    gameMap.clearCell(cell)
                    computer.units.remove(cell.unit)
                    player.money += cell.unit.getPrice()
                }
            }
        }
    }
}

fun updateCellSets(
    selectedCharacter: Character,
    gameMap: GameMap,
    cellsInMoveRange: MutableState<Set<Pair<Int, Int>>>,
    cellsInAttackRange: MutableState<Set<Pair<Int, Int>>>
) {
    // Обновляем клетки в пределах moveRange и attackRange
    val moveRangeCells = mutableSetOf<Pair<Int, Int>>()
    val attackRangeCells = mutableSetOf<Pair<Int, Int>>()

    for (i in 0 until gameMap.height) {
        for (j in 0 until gameMap.width) {
            val distance = measureDistance(
                fromX = selectedCharacter.xCoord,
                fromY = selectedCharacter.yCoord,
                toX = j,
                toY = i,
                cell = gameMap.map[i][j],
                character = selectedCharacter
            )
            if (distance <= selectedCharacter.moveRange) {
                moveRangeCells.add(Pair(j, i))
            }
            if (distance <= selectedCharacter.attackRange) {
                attackRangeCells.add(Pair(j, i))
            }
        }
    }

    cellsInMoveRange.value = moveRangeCells
    cellsInAttackRange.value = attackRangeCells
}

fun checkGameOver(player: Player, computer: Character, gameOver: MutableState<String?>) {
    if (player.health <= 0) {
        gameOver.value = "lose" // Поражение
    } else if (computer.health <= 0) {
        gameOver.value = "win" // Победа
    }
}