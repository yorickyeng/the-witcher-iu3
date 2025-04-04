package com.fk.thewitcheriu3.data

import com.fk.thewitcheriu3.data.dao.GameDao
import com.fk.thewitcheriu3.data.dto.*
import com.fk.thewitcheriu3.data.entities.GameMapEntity
import com.fk.thewitcheriu3.domain.models.Cell
import com.fk.thewitcheriu3.domain.models.GameMap
import com.fk.thewitcheriu3.domain.models.characters.heroes.Computer
import com.fk.thewitcheriu3.domain.models.characters.heroes.Hero
import com.fk.thewitcheriu3.domain.models.characters.heroes.Player
import com.fk.thewitcheriu3.domain.models.characters.units.Monster
import com.fk.thewitcheriu3.domain.models.characters.units.Unit
import com.fk.thewitcheriu3.domain.models.characters.units.Witcher
import com.fk.thewitcheriu3.domain.models.characters.units.monsters.Bruxa
import com.fk.thewitcheriu3.domain.models.characters.units.monsters.Drowner
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.GWENTWitcher
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.WolfSchoolWitcher

class GameMapRepositoryImpl(private val gameDao: GameDao) : GameMapRepository {
    override suspend fun saveGame(gameMap: GameMap, saveName: String) {
        val gameState = convertToGameState(gameMap)
        val entity = GameMapEntity(
            gameState = gameState,
            saveName = saveName
        )
        gameDao.insert(entity)
    }

    override suspend fun loadGame(id: Int): GameMap? {
        val entity = gameDao.getSaveById(id) ?: return null
        return convertFromGameState(entity.gameState)
    }

    override suspend fun getAllSaves(): List<Pair<Int, String>> {
        return gameDao.getAllSaves().map { it.id to it.saveName }
    }

    override suspend fun deleteSave(id: Int) {
        gameDao.deleteSave(id)
    }

    private fun convertToGameState(gameMap: GameMap): GameState {
        val cells = gameMap.map.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                CellState(
                    x = x,
                    y = y,
                    hero = cell.hero?.let { convertToHeroState(it) },
                    unit = cell.unit?.let { convertToUnitState(it) },
                    type = cell.type,
                )
            }.toTypedArray()
        }.toTypedArray()

        val player = convertToHeroState(gameMap.getPlayer())
        val computer = convertToHeroState(gameMap.getComputer())
        val units = gameMap.units.map { convertToUnitState(it) }

        return GameState(
            map = cells,
            player = player,
            computer = computer,
            units = units
        )
    }

    private fun convertFromGameState(gameState: GameState): GameMap {
        val gameMap = GameMap(width = gameState.map.size, height = gameState.map[0].size)

        gameState.units.forEach { unitState ->
            val unit = convertFromUnitState(unitState)
            when (unit) {
                is Witcher -> gameMap.getPlayer().units.add(unit)
                is Monster -> gameMap.getComputer().units.add(unit)
            }
        }

        gameState.map.forEachIndexed { y, row ->
            row.forEachIndexed { x, cellState ->
                val cell = Cell(
                    xCoord = x,
                    yCoord = y,
                    type = cellState.type
                )
                gameMap.map[y][x] = cell
            }
        }

        gameMap.setPlayer(convertFromHeroState(gameState.player, gameMap) as Player)
        gameMap.setComputer(convertFromHeroState(gameState.computer, gameMap) as Computer)

        gameMap.units = gameState.units.map { convertFromUnitState(it) }

        return gameMap
    }

    private fun convertToHeroState(hero: Hero): HeroState {
        return HeroState(
            type = when (hero) {
                is Player -> "player"
                is Computer -> "computer"
                else -> throw IllegalArgumentException("Unknown hero type")
            },
            name = hero.name,
            xCoord = hero.xCoord,
            yCoord = hero.yCoord,
            health = hero.health,
            money = hero.money,
        )
    }

    private fun convertFromHeroState(heroState: HeroState, gameMap: GameMap): Hero {
        return when (heroState.type) {
            "player" -> Player(
                name = heroState.name,
                xCoord = heroState.xCoord,
                yCoord = heroState.yCoord,
                gameMap = gameMap
            ).apply {
                health = heroState.health
                money = heroState.money
            }
            "computer" -> Computer(
                name = heroState.name,
                xCoord = heroState.xCoord,
                yCoord = heroState.yCoord,
                gameMap = gameMap
            ).apply {
                health = heroState.health
                money = heroState.money
            }
            else -> throw IllegalArgumentException("Unknown hero type: ${heroState.type}")
        }
    }

    private fun convertToUnitState(unit: Unit): UnitState {
        return UnitState(
            type = unit.getType(),
            xCoord = unit.xCoord,
            yCoord = unit.yCoord,
            health = unit.health,
        )
    }

    private fun convertFromUnitState(unitState: UnitState): Unit {
        return when (unitState.type) {
            "WolfSchoolWitcher" -> WolfSchoolWitcher().apply {
                xCoord = unitState.xCoord
                yCoord = unitState.yCoord
                health = unitState.health
            }
            "CatSchoolWitcher" -> CatSchoolWitcher().apply {
                xCoord = unitState.xCoord
                yCoord = unitState.yCoord
                health = unitState.health
            }
            "GWENTWitcher" -> GWENTWitcher().apply {
                xCoord = unitState.xCoord
                yCoord = unitState.yCoord
                health = unitState.health
            }
            "BearSchoolWitcher" -> BearSchoolWitcher().apply {
                xCoord = unitState.xCoord
                yCoord = unitState.yCoord
                health = unitState.health
            }
            "Drowner" -> Drowner().apply {
                xCoord = unitState.xCoord
                yCoord = unitState.yCoord
                health = unitState.health
            }
            "Bruxa" -> Bruxa().apply {
                xCoord = unitState.xCoord
                yCoord = unitState.yCoord
                health = unitState.health
            }
            else -> throw IllegalArgumentException("Unknown unit type: ${unitState.type}")
        }
    }
}