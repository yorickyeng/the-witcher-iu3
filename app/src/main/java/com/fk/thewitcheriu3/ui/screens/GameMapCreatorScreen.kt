package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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

        Column(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gameMapCreator.width),
                modifier = Modifier
                    .weight(1f)
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

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_save_24),
                            contentDescription = "save",
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(NavRoutes.NewGame.route) {
                            popUpTo(NavRoutes.MainMenu.route) { inclusive = false }
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_play_circle_filled_24),
                            contentDescription = "play",
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

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
                        Spacer(modifier = Modifier.padding(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Surface(
                                shape = RectangleShape,
                                color = Color.Transparent,
                                onClick = { viewModel.chooseType("road") },
                                modifier = Modifier.size(48.dp),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.road),
                                    contentDescription = "road",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                )
                            }

                            Surface(
                                shape = RectangleShape,
                                color = Color.Transparent,
                                onClick = { viewModel.chooseType("forest") },
                                modifier = Modifier.size(48.dp),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.forest),
                                    contentDescription = "forest",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                )
                            }

                            Surface(
                                shape = RectangleShape,
                                color = Color.Transparent,
                                onClick = { viewModel.chooseCastle() },
                                modifier = Modifier.size(48.dp),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.castle),
                                    contentDescription = "castle",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                )
                            }
                        }
                    }

                    "castle" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Surface(
                                shape = RectangleShape,
                                color = Color.Transparent,
                                onClick = {
                                    viewModel.chooseType("Kaer Morhen")
                                    viewModel.chooseCastle()
                                },
                                modifier = Modifier.size(48.dp),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.kaer_morhen),
                                    contentDescription = "Kaer Morhen",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                )
                            }

                            Surface(
                                shape = RectangleShape,
                                color = Color.Transparent,
                                onClick = {
                                    viewModel.chooseType("Zamek Stygga")
                                    viewModel.chooseCastle()
                                },
                                modifier = Modifier.size(48.dp),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.stygga),
                                    contentDescription = "Zamek Stygga",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                )
                            }

                            IconButton(onClick = { viewModel.goBack() }) {
                                Image(
                                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                                    contentDescription = "Back",
                                )
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.BottomCenter) {
                IconButton(onClick = { navController.navigate(NavRoutes.MainMenu.route) }) {
                    Icon(
                        painter = painterResource(R.drawable.main_menu),
                        contentDescription = "main menu",
                    )
                }
            }
        }
    }
}