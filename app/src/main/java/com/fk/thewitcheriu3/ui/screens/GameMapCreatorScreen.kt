package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.fk.thewitcheriu3.domain.models.NavRoutes
import com.fk.thewitcheriu3.ui.components.CellView
import com.fk.thewitcheriu3.ui.components.MainMenuButton
import com.fk.thewitcheriu3.ui.viewmodels.GameMapCreatorViewModel

@Composable
fun GameMapCreatorScreen(
    navController: NavController,
    viewModel: GameMapCreatorViewModel = viewModel(),
) {
    val gameMapCreator = viewModel.gameMapCreator
    viewModel.selectedType

    val systemBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.game_background),
            contentDescription = "Game Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(gameMapCreator.width),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = systemBarPadding)
        ) {
            items(gameMapCreator.height * gameMapCreator.width) { index ->
                val x = index % gameMapCreator.width
                val y = index / gameMapCreator.width
                val cell = gameMapCreator.map[y][x]

                CellView(cell = cell, onClick = {
                    viewModel.handleCellClick(cell)
                })
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 450.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (viewModel.currentMenu) {
                "main" -> {
                    Text(
                        text = "Choose map objects",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    MainMenuButton("Road") { viewModel.chooseType("road") }
                    MainMenuButton("Forest") { viewModel.chooseType("forest") }
                    MainMenuButton("Castle") { viewModel.chooseCastle() }
                }

                "castle" -> {
                    MainMenuButton("Kaer Morhen") {
                        viewModel.chooseType("Kaer Morhen")
                        viewModel.chooseCastle()
                    }
                    MainMenuButton("Zamek Stygga") {
                        viewModel.chooseType("Zamek Stygga")
                        viewModel.chooseCastle()
                    }
                    MainMenuButton("Back") { viewModel.goBack() }
                }
            }

            Spacer(Modifier.padding(vertical = 10.dp))

            MainMenuButton(
                text = "To Main Menu",
                onClick = { navController.navigate(NavRoutes.MainMenu.route) }
            )
        }

        Box(modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(top = 40.dp)) {
            MainMenuButton("Save") {}
        }

        Box(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(top = 40.dp)) {
            MainMenuButton("Play") {
                navController.navigate(NavRoutes.NewGame.route) {
                    popUpTo(NavRoutes.MainMenu.route) { inclusive = false }
                }
            }
        }
    }
}