package com.fk.thewitcheriu3.domain

import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Computer
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Player
import kotlin.math.max
import kotlin.math.min

fun computersTurn(
    gameMap: GameMap, player: Player, computer: Computer, gameOver: MutableState<String?>
) {
    val monsterTypes = arrayListOf("Drowner", "Bruxa")
    buyUnit(gameMap, computer, monsterTypes.random())

    computerMoves(gameMap, player, computer, gameOver)
    computerAttacks(gameMap, player, computer, gameOver)
    for (i in 0 until computer.units.size) {
        computerMoves(gameMap, player, computer.units[i], gameOver)
        computerAttacks(gameMap, player, computer.units[i], gameOver)
    }
}

fun computerMoves(
    gameMap: GameMap, player: Player, computer: Character, gameOver: MutableState<String?>
) {
    val (computerX, computerY) = computer.getPosition()

    // Определяем границы для xRandom и yRandom
    val minX = max(0, computerX - computer.moveRange)
    val maxX = min(gameMap.width - 1, computerX + computer.moveRange)
    val minY = max(0, computerY - computer.moveRange)
    val maxY = min(gameMap.height - 1, computerY + computer.moveRange)

    val xRandom = getRandomCoords(gameMap, minX, maxX, minY, maxY).first
    val yRandom = getRandomCoords(gameMap, minX, maxX, minY, maxY).second
    val distance = measureDistance(
        fromX = computerX,
        fromY = computerY,
        toX = xRandom,
        toY = yRandom,
        gameMap.map[yRandom][xRandom],
        computer
    )

    if (distance <= computer.moveRange) {
        computer.move(gameMap, xRandom, yRandom)
    }

    if (xRandom == 0 && yRandom == 0) {
        player.health = 0
        checkGameOver(player, computer, gameOver)
    }
}

fun computerAttacks(
    gameMap: GameMap, player: Player, computer: Character, gameOver: MutableState<String?>
) {
    // Поиск цели для атаки
    val target = findAttackTarget(gameMap, player, computer)
    if (target != null) {
        computer.attack(gameMap, target, gameOver)
    }
}

// Функция для поиска цели атаки
fun findAttackTarget(gameMap: GameMap, player: Player, computer: Character): Character? {
    val (computerX, computerY) = computer.getPosition()
    val (playerX, playerY) = player.getPosition()

    // Проверяем юнитов игрока
    for (unit in player.units) {
        val (unitX, unitY) = unit.getPosition()
        val distance = measureDistance(
            fromX = computerX,
            fromY = computerY,
            toX = unitX,
            toY = unitY,
            cell = gameMap.map[unitY][unitX],
            character = computer
        )
        if (distance <= computer.attackRange) {
            return unit // Атакуем юнит игрока
        }
    }

    // Проверяем героя игрока
    val distance = measureDistance(
        fromX = computerX,
        fromY = computerY,
        toX = playerX,
        toY = playerY,
        cell = gameMap.map[playerY][playerX],
        character = computer
    )
    if (distance <= computer.attackRange) {
        return player // Атакуем героя игрока
    }

    return null // Нет целей для атаки
}