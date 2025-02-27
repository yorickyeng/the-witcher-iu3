package com.fk.thewitcheriu3.domain

import androidx.compose.runtime.MutableState
import com.fk.thewitcheriu3.domain.entities.Character
import com.fk.thewitcheriu3.domain.entities.Computer
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.Player
import com.fk.thewitcheriu3.domain.entities.Witcher
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun computersTurn(gameMap: GameMap, player: Player, computer: Computer, gameOver: MutableState<String?>) {
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

    var xRandom: Int
    var yRandom: Int
    do {
        xRandom = Random.nextInt(minX, maxX)
        yRandom = Random.nextInt(minY, maxY)
    } while ((abs(xRandom - computerX) + abs(yRandom - computerY) > computer.moveRange) ||
        (gameMap.map[yRandom][xRandom].hero != null) || (gameMap.map[yRandom][xRandom].unit != null))

    computer.move(
        gameMap, xRandom, yRandom
    )

    if (xRandom == 0 && yRandom == 0) {
        player.health = 0
        checkGameOver(player, computer, gameOver)
    }
}

fun computerAttacks(
    gameMap: GameMap, player: Player, computer: Character, gameOver: MutableState<String?>
) {
    // Поиск цели для атаки
    val attackTarget = findAttackTarget(computer, player)
    if (attackTarget != null) {
        computer.attack(attackTarget)
        if (attackTarget.health <= 0) {
            val cell = gameMap.map[attackTarget.yCoord][attackTarget.xCoord]
            gameMap.clearCell(cell)
            if (attackTarget is Witcher) {
                player.units.remove(attackTarget)
                if (computer is Computer) {
                    computer.money += attackTarget.getPrice()
                }
            }
            if (attackTarget is Player) {
                checkGameOver(player, computer, gameOver)
            }
        }
    }
}

// Функция для поиска цели атаки
fun findAttackTarget(computer: Character, player: Player): Character? {
    val (computerX, computerY) = computer.getPosition()

    // Проверяем юнитов игрока
    for (unit in player.units) {
        val (unitX, unitY) = unit.getPosition()
        val distance = abs(computerX - unitX) + abs(computerY - unitY)
        if (distance <= computer.attackRange) {
            return unit // Атакуем юнит игрока
        }
    }

    // Проверяем героя игрока
    val (playerX, playerY) = player.getPosition()
    val distance = abs(computerX - playerX) + abs(computerY - playerY)
    if (distance <= computer.attackRange) {
        return player // Атакуем героя игрока
    }

    return null // Нет целей для атаки
}