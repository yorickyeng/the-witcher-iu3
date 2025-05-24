package com.fk.thewitcheriu3.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.fk.thewitcheriu3.ui.viewmodels.GameMapViewModel

@Composable
fun GameMapScreen(
    navController: NavController, viewModel: GameMapViewModel = viewModel()
) {
    val context = LocalContext.current
    val gameMap = viewModel.gameMap
    val selectedCell = viewModel.selectedCell
    val cellsInMoveRange = viewModel.cellsInMoveRange
    val cellsInAttackRange = viewModel.cellsInAttackRange
    val inKaerMorhen = viewModel.inKaerMorhen
    val gameOver = viewModel.gameOver
    val showRaccoon = viewModel.showRaccoon
    val playersMoney = viewModel.playersMoney
    val movesCounter = viewModel.movesCounter
    val saveName = viewModel.saveName
    var inTavern = viewModel.inTavern
    val movePoints = gameMap.getPlayer().units.size + 1 - movesCounter.intValue
    val showSpotsScreen = viewModel.showSpotsScreen
    val currentSpotType = viewModel.currentSpotType

    val systemBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LaunchedEffect(Unit) {
        viewModel.startRaccoonTimer()
    }

    val message = viewModel.getToastMessage()
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
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

        if (inKaerMorhen.value) {
            KaerMorhenScreen(
                onPlayGwent = { navController.navigate(NavRoutes.Gwent.route) },
                onBuy = { viewModel.playerBuysUnit(it) },
                onShowSpots = { viewModel.showSpots(it) },
                onClose = { inKaerMorhen.value = false })
        }

        if (inTavern.value) {
            TavernScreen(
                onPlayGwent = { navController.navigate(NavRoutes.Gwent.route) },
                onShowSpots = { viewModel.showSpots(it) },
                onClose = { inTavern.value = false })
        }

        if (showSpotsScreen.value && currentSpotType.value != null) {
            viewModel.updateAvailableSpots()
            SpotsScreen(
                spots = viewModel.availableSpots.value,
                spotType = currentSpotType.value!!,
                onSpotSelected = { viewModel.occupySpot(it) },
                onClose = { viewModel.hideSpots() },
                getOccupationTime = { spot -> viewModel.getSpotOccupationTime(spot) },
                inTavern = inTavern.value,
            )
        }

        if (!inKaerMorhen.value && !showRaccoon.value && !inTavern.value && gameOver.value == null) {
            if (selectedCell.value != null) {
                val (x, y) = selectedCell.value!!
                val info = viewModel.getCellInfo()
                val selectedCharacter = viewModel.selectedCharacter
                val activityInfo = selectedCharacter?.let { viewModel.getCharacterActivityInfo(it) }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "$info\n($x, $y)",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    activityInfo?.let {
                        Text(
                            text = it,
                            fontSize = 18.sp,
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

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
                IconButton(
                    onClick = { viewModel.saveGame(saveName) }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_save_24),
                        contentDescription = "save",
                    )
                }

                Text(
                    text = "Move points left: ${movePoints}\n",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )

                IconButton(
                    onClick = { navController.navigate(NavRoutes.SaveLoadMenu.route) }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_upload_24),
                        contentDescription = "load",
                    )
                }
            }
        }

        Text(
            text = "${viewModel.getFormattedGameTime()}\n$playersMoney orens",
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