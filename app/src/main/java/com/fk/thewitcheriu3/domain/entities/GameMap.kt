package com.fk.thewitcheriu3.domain.entities

import com.fk.thewitcheriu3.domain.entities.characters.heroes.Computer
import com.fk.thewitcheriu3.domain.entities.characters.Character
import com.fk.thewitcheriu3.domain.entities.characters.heroes.Player
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Bruxa
import com.fk.thewitcheriu3.domain.entities.characters.units.monsters.Drowner
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.characters.units.witchers.WolfSchoolWitcher
import kotlin.random.Random

class GameMap(
    val width: Int, val height: Int
) {
    val map: Array<Array<Cell>> = Array(height) { i ->
        Array(width) { j ->
            Cell(type = "field", xCoord = j, yCoord = i)
        }
    }

    private var Ciri: Player
    private var Vilgefortz: Computer

    fun getPlayer() = Ciri
    fun getComputer() = Vilgefortz

    private var deathNote = mutableListOf<Character>()
    fun died(character: Character) {
        deathNote.add(character)
    }

    fun anybodyDied() = deathNote.isNotEmpty()
    fun twoGuysDied() = deathNote.size >= 2
    fun getDeathNoteSize() = deathNote.size
    fun resurrect() {
        if (anybodyDied()) {
            val randomIndex = Random.nextInt(0, deathNote.size)
            deathNote[randomIndex].place(this)
            deathNote.removeAt(randomIndex)
        }
    }

    fun updateCell(cell: Cell) {
        map[cell.yCoord][cell.xCoord] = cell
    }

    fun clearCell(cell: Cell) {
        updateCell(
            Cell(
                type = cell.type,
                hero = null,
                unit = null,
                xCoord = cell.xCoord,
                yCoord = cell.yCoord
            )
        )
    }

    init { // map initialization
        for (i in 0 until width) {
            updateCell(Cell(type = "road", xCoord = i, yCoord = i))
        }

        for (i in 0 until height / 2) {
            for (j in width / 2 until width) {
                if (j >= i + 5) {
                    updateCell(Cell(type = "forest", xCoord = i, yCoord = j))
                }
            }
        }

        for (i in height / 2 until height) {
            for (j in 0 until width) {
                if (j <= i - 5) {
                    updateCell(Cell(type = "forest", xCoord = i, yCoord = j))
                }
            }
        }

        updateCell(Cell(type = "castle", xCoord = 0, yCoord = 0))
        updateCell(Cell(type = "castle", xCoord = width - 1, yCoord = height - 1))

        Ciri = Player(name = "Cirilla Fiona Elen Riannon", 0, 1, this)
        Vilgefortz = Computer(name = "Vilgefortz of Roggeveen", 9, 8, this)

        val Gaetan = CatSchoolWitcher()
        Gaetan.place(this)
        Ciri.addUnit(Gaetan)

        val Geralt = WolfSchoolWitcher()
        Geralt.place(this)
        Ciri.addUnit(Geralt)

        for (i in 5 until 8) {
            val drowner = Drowner()
            drowner.place(this)
            Vilgefortz.addUnit(drowner)
        }

        val bruxa = Bruxa()
        bruxa.place(this)
        Vilgefortz.addUnit(bruxa)
    }
}