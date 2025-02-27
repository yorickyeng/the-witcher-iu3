package com.fk.thewitcheriu3.domain

import com.fk.thewitcheriu3.domain.entities.GameMap
import kotlin.random.Random

fun getRandomCoords(gameMap: GameMap, fromX: Int, untilX: Int, fromY: Int, untilY: Int): Pair<Int, Int> {
    var xRandom: Int
    var yRandom: Int

    do {
        xRandom = Random.nextInt(fromX, untilX)
        yRandom = Random.nextInt(fromY, untilY)
    } while (gameMap.map[yRandom][xRandom].hero != null || gameMap.map[yRandom][xRandom].unit != null)

    return Pair(xRandom, yRandom)
}