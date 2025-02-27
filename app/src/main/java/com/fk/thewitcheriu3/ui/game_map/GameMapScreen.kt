package com.fk.thewitcheriu3.ui.game_map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.buyUnit
import com.fk.thewitcheriu3.domain.entities.Cell
import com.fk.thewitcheriu3.domain.entities.Character
import com.fk.thewitcheriu3.domain.entities.heroes.Computer
import com.fk.thewitcheriu3.domain.entities.GameMap
import com.fk.thewitcheriu3.domain.entities.units.Monster
import com.fk.thewitcheriu3.domain.entities.heroes.Player
import com.fk.thewitcheriu3.domain.entities.units.Witcher
import com.fk.thewitcheriu3.domain.selectedCharacterActions
import com.fk.thewitcheriu3.domain.updateCellSets
import com.fk.thewitcheriu3.ui.GameOverScreen
import kotlinx.coroutines.delay

@Composable
fun GameMapScreen() {
    var gameMap by remember { mutableStateOf(GameMap(10, 10)) }
    var player by remember { mutableStateOf(gameMap.getPlayer()) }
    var computer by remember { mutableStateOf(gameMap.getComputer()) }

    val selectedCell = remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val selectedCharacter = remember { mutableStateOf<Character?>(null) }
    val cellsInMoveRange = remember { mutableStateOf<Set<Pair<Int, Int>>>(emptySet()) }
    val cellsInAttackRange = remember { mutableStateOf<Set<Pair<Int, Int>>>(emptySet()) }

    val movesCounter = remember { mutableIntStateOf(0) }
    val showBuyMenu = remember { mutableStateOf(false) }
    val gameOver = remember { mutableStateOf<String?>(null) }

    val monsterTypes = arrayListOf("Drowner", "Bruxa")
    LaunchedEffect(Unit) {
        delay(10000)
        buyUnit(gameMap, computer, monsterTypes.random())
    } // раз в 10 секунд появляется случайный монстр компа (если у него хватит денег)

    // Функция для обработки покупки юнита
    fun handleBuyUnit(unitType: String) {
        buyUnit(gameMap, player, unitType)
        showBuyMenu.value = false
    }

    // Функция для обработки клика по клетке
    fun handleCellClick(cell: Cell) {
        selectedCell.value = Pair(cell.xCoord, cell.yCoord)

        selectedCharacter.value?.let { selectedChar ->
            // Если персонаж уже выбран
            selectedCharacterActions(
                selectedChar, cell, gameMap, player, computer, movesCounter, gameOver, showBuyMenu
            )

            selectedCharacter.value = null // Сброс выбранного персонажа
            selectedCell.value = null // Сброс выбранной клетки
            cellsInMoveRange.value = emptySet() // Сброс выделения moveRange
            cellsInAttackRange.value = emptySet() // Сброс выделения attackRange
        } ?: run {
            // Если персонаж ещё не выбран
            when {
                cell.hero is Player -> selectedCharacter.value = cell.hero
                cell.hero is Computer -> selectedCharacter.value = cell.hero
                cell.unit is Witcher -> selectedCharacter.value = cell.unit
                cell.unit is Monster -> selectedCharacter.value = cell.unit
            }

            // Обновляем клетки в пределах moveRange и attackRange
            selectedCharacter.value?.let { char ->
                updateCellSets(char, gameMap, cellsInMoveRange, cellsInAttackRange)
            }
        }
    }

    fun refreshGame() {
        gameMap = GameMap(10, 10)
        player = gameMap.getPlayer()
        computer = gameMap.getComputer()

        selectedCell.value = null
        selectedCharacter.value = null
        cellsInMoveRange.value = emptySet()
        cellsInAttackRange.value = emptySet()

        movesCounter.intValue = 0
        showBuyMenu.value = false
        gameOver.value = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseSurface)
    ) {
        Image(
            painter = painterResource(R.drawable.game_background),
            contentDescription = "Game Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(gameMap.width),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 22.dp)
        ) {
            items(gameMap.height * gameMap.width) { index ->
                val x = index % gameMap.width
                val y = index / gameMap.width
                val cell = gameMap.map[y][x]

                CellView(cell = cell,
                    selectedCell = selectedCell.value,
                    cellsInMoveRange = cellsInMoveRange.value,
                    cellsInAttackRange = cellsInAttackRange.value,
                    onClick = { handleCellClick(cell) })
            }
        }

        if (showBuyMenu.value) {
            BuyMenuScreen(onBuy = { handleBuyUnit(it) }, onClose = { showBuyMenu.value = false })
        }

        if (selectedCell.value != null) {
            val (x, y) = selectedCell.value!!
            val cell = gameMap.map[y][x]
            val info = when {
                cell.hero != null -> cell.hero.toString()
                cell.unit != null -> cell.unit.toString()
                else -> cell.type
            }

            Text(
                text = "$info\nCell ($x, $y)",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }

        Text(
            text = "Your money: ${player.money} orens",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        GameOverScreen(gameOver.value, onClick = { refreshGame() })
    }
}