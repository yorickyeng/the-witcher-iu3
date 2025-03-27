package com.fk.thewitcheriu3.domain.entities

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class GameMapCreator(val width: Int, val height: Int) {
    val map: SnapshotStateList<SnapshotStateList<Cell>> = mutableStateListOf()

    init {
        repeat(height) { y ->
            val row = mutableStateListOf<Cell>()
            repeat(width) { x ->
                row.add(Cell(type = "field", xCoord = x, yCoord = y))
            }
            map.add(row)
        }
    }

    fun updateCell(cell: Cell) {
        map[cell.yCoord][cell.xCoord] = cell
    }
}