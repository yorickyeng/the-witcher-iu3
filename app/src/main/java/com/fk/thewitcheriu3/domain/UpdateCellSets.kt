package com.fk.thewitcheriu3.domain

import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.Character

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