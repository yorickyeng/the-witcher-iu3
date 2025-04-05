package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.RaccoonComing
import com.fk.thewitcheriu3.domain.models.NavRoutes
import com.fk.thewitcheriu3.ui.components.CellView
import com.fk.thewitcheriu3.ui.components.MainMenuButton
import com.fk.thewitcheriu3.ui.viewmodels.GameMapViewModel

@Composable
fun GameMapScreen(
    navController: NavController, viewModel: GameMapViewModel = viewModel()
) {

    val gameMap = viewModel.gameMap
    val selectedCell = viewModel.selectedCell
    val cellsInMoveRange = viewModel.cellsInMoveRange
    val cellsInAttackRange = viewModel.cellsInAttackRange
    val showBuyMenu = viewModel.showBuyMenu
    val gameOver = viewModel.gameOver
    val showRaccoon = viewModel.showRaccoon
    val playersMoney = viewModel.playersMoney
    val movesCounter = viewModel.movesCounter
    val saveName = viewModel.saveName
    val movePoints = gameMap.getPlayer().units.size + 1 - movesCounter.intValue

    val systemBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LaunchedEffect(Unit) {
        viewModel.startRaccoonTimer()
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                .padding(vertical = systemBarPadding)
        ) {
            items(gameMap.height * gameMap.width) { index ->
                val x = index % gameMap.width
                val y = index / gameMap.width
                val cell = gameMap.map[y][x]

                CellView(
                    cell = cell,
                    selectedCell = selectedCell.value,
                    cellsInMoveRange = cellsInMoveRange.value,
                    cellsInAttackRange = cellsInAttackRange.value,
                    onClick = { viewModel.handleCellClick(cell) })
            }
        }

        if (showRaccoon.value) {
            RaccoonComing(showRaccoon.value)
        }

        if (showBuyMenu.value) {
            BuyMenuScreen(
                onBuy = { viewModel.playerBuysUnit(it) },
                onClose = { showBuyMenu.value = false })
        }

        if (selectedCell.value != null) {
            val (x, y) = selectedCell.value!!
            val info = viewModel.getCellInfo()

            Text(
                text = "$info\nCell ($x, $y)",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }

        if (!showBuyMenu.value && !showRaccoon.value && gameOver.value == null) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = systemBarPadding,
                        start = 4.dp,
                        end = 4.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                MainMenuButton("Save") { viewModel.saveGame(saveName) }

                Text(
                    text = "Move points left: ${movePoints}\n",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )

                MainMenuButton("Load") { navController.navigate(NavRoutes.SaveLoadMenu.route) }
            }
        }

        Text(
            text = "Your money: $playersMoney orens",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        if (gameOver.value != null) {
            GameOverScreen(gameOver.value, onClick = { viewModel.refreshGame() })
        }
    }
}