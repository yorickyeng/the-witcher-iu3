package com.fk.thewitcheriu3.domain.entities

import com.fk.thewitcheriu3.R
import kotlin.math.abs
import kotlin.random.Random

abstract class Unit protected constructor(
    private val type: String,
    override var health: Int,
    override val damage: Int,
    override val moveRange: Int,
    override val attackRange: Int,
    private val price: Int
) : Character {

    abstract override var xCoord: Int
    abstract override var yCoord: Int
    abstract override val texture: Int

    fun getType() = type
    fun getPrice() = price

    override fun place(gameMap: GameMap) {
        gameMap.updateCell(
            Cell(
                type = gameMap.map[yCoord][xCoord].type,
                unit = this,
                xCoord = xCoord,
                yCoord = yCoord
            )
        )
    }

    override fun move(gameMap: GameMap, x: Int, y: Int) {
        val distance = abs(x - xCoord) + abs(y - yCoord)
        if (distance <= moveRange) {
            super.move(gameMap, xCoord, yCoord)
            gameMap.updateCell(
                Cell(
                    type = gameMap.map[y][x].type, hero = null, unit = this, xCoord = x, yCoord = y
                )
            )
            xCoord = x
            yCoord = y
        }
    }

    override fun toString(): String {
        return "\n$type\nHealth $health\nDamage $damage\nMove Range $moveRange\nAttack Range $attackRange"
    }
}

abstract class Witcher(
    type: String = "Witcher",
    health: Int,
    damage: Int,
    moveRange: Int = 3,
    attackRange: Int,
    price: Int
) : Unit(
    type, health, damage, moveRange, attackRange, price
) {
    final override var xCoord: Int = 1
    final override var yCoord: Int = 1

    final override fun place(gameMap: GameMap) {
        do {
            xCoord = Random.nextInt(4)
            yCoord = Random.nextInt(4)
        } while (gameMap.map[yCoord][xCoord].hero != null && gameMap.map[yCoord][xCoord].unit != null)

        super.place(gameMap)
    }
}

class CatSchoolWitcher : Witcher(
    type = "Cat School Witcher",
    health = 75,
    damage = 100,
    moveRange = 6,
    attackRange = 4,
    price = 100
) {
    override val texture = R.drawable.gaetan
}

class WolfSchoolWitcher : Witcher(
    type = "Wolf School Witcher", health = 100, damage = 150, attackRange = 3, price = 150
) {
    override val texture = R.drawable.geralt
}

class BearSchoolWitcher : Witcher(
    type = "Bear School Witcher", health = 200, damage = 200, attackRange = 2, price = 200
) {
    override val texture = R.drawable.bear
}

class GWENTWitcher : Witcher(
    type = "GWENT Witcher",
    health = 10000,
    damage = 10000,
    moveRange = 10,
    attackRange = 100,
    price = 1000
) {
    override val texture = R.drawable.gwent
}

abstract class Monster(
    type: String = "Monster",
    health: Int,
    damage: Int,
    moveRange: Int = 4,
    attackRange: Int,
    price: Int
) : Unit(
    type, health, damage, moveRange, attackRange, price
) {
    final override var xCoord: Int = 8
    final override var yCoord: Int = 8

    final override fun place(gameMap: GameMap) {
        var xRandom: Int
        var yRandom: Int

        do {
            xRandom = Random.nextInt(gameMap.width - 4, gameMap.width)
            yRandom = Random.nextInt(gameMap.height - 4, gameMap.height)
        } while (gameMap.map[yRandom][xRandom].hero != null || gameMap.map[yRandom][xRandom].unit != null)

        xCoord = xRandom
        yCoord = yRandom
        super.place(gameMap)
    }
}

class Drowner : Monster(
    type = "Drowner", health = 50, damage = 50, attackRange = 3, price = 50
) {
    override val texture = R.drawable.drowner
}

abstract class Vampire(
    type: String = "Vampire",
    health: Int,
    damage: Int,
    moveRange: Int = 2,
    attackRange: Int,
    price: Int
) : Monster(
    type, health, damage, moveRange, attackRange, price
)

class Bruxa : Vampire(
    type = "Bruxa", health = 150, damage = 100, attackRange = 2, price = 200
) {
    override val texture = R.drawable.bruxa
}