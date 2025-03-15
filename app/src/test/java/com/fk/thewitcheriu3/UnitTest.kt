package com.fk.thewitcheriu3

import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.penalty
import com.fk.thewitcheriu3.ui.game_map.GameMapViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    private val gameMap = GameMap(10, 10)
    private val viewModel = GameMapViewModel()

    @Test
    fun `addition is correct`() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `penalty is correct`() {
        val cell = mockk<Cell>()
        val character = mockk<CatSchoolWitcher>()
        every { cell.type } returns "forest"
        assertEquals(0, penalty(cell, character))
    }

    @Test
    fun `player wins by capturing enemy castle`() {
        // arrange
        val player = gameMap.getPlayer()
        // act
        player.move(gameMap, 9, 9)
        // assert
        assertEquals("win", gameMap.checkGameOver())
    }

    @Test
    fun `computer wins by capturing player castle`() {
        val computer = gameMap.getComputer()
        val playerCastle = gameMap.map[0][0]

        computer.move(gameMap, playerCastle.xCoord, playerCastle.yCoord)
        assertEquals("lose", gameMap.checkGameOver())
    }

    @Test
    fun `check game over player wins`() {
        gameMap.getComputer().health = 0
        assertEquals("win", gameMap.checkGameOver())
    }

    @Test
    fun `check game over player loses`() {
        gameMap.getPlayer().health = 0
        assertEquals("lose", gameMap.checkGameOver())
    }

    @Test
    fun `check game over game continues`() {
        assertEquals(null, gameMap.checkGameOver())
    }

    @Test
    fun `game over win character attacks target dies`() {
        val attacker = gameMap.getPlayer()
        val target = gameMap.getComputer()

        target.health = 10
        attacker.attack(target)

        assertEquals("win", gameMap.characterAttacks(attacker, target))
    }

    @Test
    fun `character attacks target survives`() {
        val attacker = gameMap.getPlayer()
        val target = gameMap.getComputer()

        target.health = 10000
        attacker.attack(target)

        assertEquals(null, gameMap.characterAttacks(attacker, target))
    }

    @Test
    fun `buy unit success`() {
        val player = gameMap.getPlayer()
        player.money = 100
        assertTrue(gameMap.buyUnit(player, "Cat School Witcher"))
    }

    @Test
    fun `buy unit failure`() {
        val player = gameMap.getPlayer()
        player.money = 99
        assertFalse(gameMap.buyUnit(player, "Cat School Witcher"))
    }

    @Test
    fun `update range cells`() {
        val character = gameMap.getPlayer()
        val (moveRangeCells, attackRangeCells) = gameMap.updateRangeCells(character)

        assertTrue(moveRangeCells.isNotEmpty())
        assertTrue(attackRangeCells.isNotEmpty())
    }

    @Test
    fun `handle cell click select character`() {
        val cell = viewModel.gameMap.map[1][0]

        viewModel.handleCellClick(cell)

        assertEquals(viewModel.player, viewModel.selectedCharacter)
    }

    @Test
    fun `handle cell click move character`() {
        val player = viewModel.player
        val targetCell = viewModel.gameMap.map[1][1]
        viewModel.allyMoves(player, targetCell)

        assertEquals(1, viewModel.movesCounter.intValue)
    }

    @Test
    fun `check resurrect health`() {
        val deadCharacter = gameMap.getPlayer()
        deadCharacter.health = 0
        gameMap.died(deadCharacter)

        gameMap.resurrect()

        assertTrue(deadCharacter.health == 100)
    }

    @Test
    fun `check new game +`() {
        viewModel.refreshGame()
        assertEquals(0, viewModel.movesCounter.intValue)
    }

    @Test
    fun `battlefield display castle`() {
        val cell = gameMap.map[0][0]

        assertEquals("Kaer Morhen", cell.type)
    }

    @Test
    fun `computer units size change after buying`() {
        val computer = gameMap.getComputer()

        val initialUnitCount = computer.units.size
        gameMap.buyUnit(computer, "Drowner")
        assertTrue(computer.units.size > initialUnitCount)
    }

    @Test
    fun `enemy death`() {
        val player = gameMap.getPlayer()
        val enemy = gameMap.getComputer()

        enemy.health = 1
        player.attack(enemy)
        assertTrue(enemy.health <= 0)
    }

    @Test
    fun `movement to occupied cell`() {
        val player = gameMap.getPlayer()
        val enemy = gameMap.getComputer()
        enemy.health = 10000
        viewModel.selectedCharacterMoveAndAttackLogic(player, gameMap.map[enemy.yCoord][enemy.xCoord])

        assertFalse(player.getPosition() == enemy.getPosition())
    }

    @Test
    fun `health is less after attack execution`() {
        val player = gameMap.getPlayer()
        val enemy = gameMap.getComputer()
        val initialHealth = enemy.health

        player.attack(enemy)
        assertTrue(enemy.health < initialHealth)
    }

}