package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.NavRoutes
import com.fk.thewitcheriu3.domain.models.gwent.*
import com.fk.thewitcheriu3.ui.viewmodels.GwentViewModel

@Composable
fun GwentGameScreen(
    navController: NavController,
    viewModel: GwentViewModel
) {
    val gameState = viewModel.gameState

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.gwent_field),
            contentDescription = "Game Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier.fillMaxSize()) {
            // Перемещаем боковую панель влево
            ScorePanel(
                gameState = gameState,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(8.dp)
            )

            // Основное поле игры
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Поле противника
                AiField(
                    field = gameState?.aiField ?: emptyMap(),
                    weatherEffects = gameState?.weatherEffects ?: emptyList(),
                    modifier = Modifier.weight(1f)
                )

                // Погодные эффекты
                WeatherEffects(
                    effects = gameState?.weatherEffects ?: emptyList(),
                    modifier = Modifier.height(60.dp)
                )

                // Поле игрока
                PlayerField(
                    field = gameState?.playerField ?: emptyMap(),
                    weatherEffects = gameState?.weatherEffects ?: emptyList(),
                    modifier = Modifier.weight(1f)
                )

                // Рука игрока
                PlayerHand(
                    cards = gameState?.playerHand ?: emptyList(),
                    onCardClick = { viewModel.playCard(it) },
                    modifier = Modifier.height(150.dp)
                )

                Button(
                    onClick = { viewModel.pass() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                ) {
                    Text("Pass")
                }
            }
        }

        // Показываем результат раунда
        if (viewModel.showRoundResult && gameState?.roundWinner != null && !gameState.isGameOver) {
            RoundResultDialog(
                winner = gameState.roundWinner,
                playerScore = gameState.lastRoundPlayerScore,
                aiScore = gameState.lastRoundAiScore,
                playerRoundsWon = gameState.playerRoundsWon,
                aiRoundsWon = gameState.aiRoundsWon,
                currentRound = gameState.currentRound,
                onContinue = { viewModel.continueGame() }
            )
        }

        // Показываем диалог окончания игры
        if (gameState?.isGameOver == true) {
            GameOverDialog(
                playerRoundsWon = gameState.playerRoundsWon,
                aiRoundsWon = gameState.aiRoundsWon,
                finalPlayerScore = gameState.finalPlayerScore,
                finalAiScore = gameState.finalAiScore,
                onNewGame = { viewModel.startNewGame() },
                onExit = { navController.navigate(NavRoutes.MainMenu.route) }
            )
        }
    }
}

@Composable
fun PlayerHand(
    cards: List<GwentCard>,
    onCardClick: (GwentCard) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(cards) { card ->
            GwentCardView(
                card = card,
                onClick = { onCardClick(card) }
            )
        }
    }
}

@Composable
fun GwentCardView(
    card: GwentCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(100.dp)
            .height(140.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(card.imageRes),
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (card.row != GwentCardRow.WEATHER) {
                Text(
                    text = card.power.toString(),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(4.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PlayerField(
    field: Map<GwentCardRow, List<GwentCard>>,
    weatherEffects: List<WeatherEffect>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BattleRow(
            cards = field[GwentCardRow.SIEGE] ?: emptyList(),
            isAffectedByWeather = WeatherEffect.RAIN in weatherEffects,
            rowType = GwentCardRow.SIEGE
        )
        BattleRow(
            cards = field[GwentCardRow.RANGED] ?: emptyList(),
            isAffectedByWeather = WeatherEffect.FOG in weatherEffects,
            rowType = GwentCardRow.RANGED
        )
        BattleRow(
            cards = field[GwentCardRow.MELEE] ?: emptyList(),
            isAffectedByWeather = WeatherEffect.FROST in weatherEffects,
            rowType = GwentCardRow.MELEE
        )
    }
}

@Composable
fun AiField(
    field: Map<GwentCardRow, List<GwentCard>>,
    weatherEffects: List<WeatherEffect>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BattleRow(
            cards = field[GwentCardRow.MELEE] ?: emptyList(),
            isAffectedByWeather = WeatherEffect.FROST in weatherEffects,
            rowType = GwentCardRow.MELEE
        )
        BattleRow(
            cards = field[GwentCardRow.RANGED] ?: emptyList(),
            isAffectedByWeather = WeatherEffect.FOG in weatherEffects,
            rowType = GwentCardRow.RANGED
        )
        BattleRow(
            cards = field[GwentCardRow.SIEGE] ?: emptyList(),
            isAffectedByWeather = WeatherEffect.RAIN in weatherEffects,
            rowType = GwentCardRow.SIEGE
        )
    }
}

@Composable
fun BattleRow(
    cards: List<GwentCard>,
    isAffectedByWeather: Boolean,
    rowType: GwentCardRow,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(vertical = 4.dp)
    ) {
        // Добавляем подпись ряда
        Text(
            text = when (rowType) {
                GwentCardRow.SIEGE -> "Siege Units"
                GwentCardRow.RANGED -> "Ranged Units"
                GwentCardRow.MELEE -> "Melee Units"
                else -> ""
            },
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    if (isAffectedByWeather) 
                        Color.Gray.copy(alpha = 0.5f) 
                    else 
                        Color.Transparent
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(cards) { card ->
                GwentCardView(
                    card = card,
                    onClick = { /* Nothing */ }
                )
            }
        }
    }
}

@Composable
fun WeatherEffects(
    effects: List<WeatherEffect>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(effects) { effect ->
            Image(
                painter = painterResource(
                    when (effect) {
                        WeatherEffect.FROST -> R.drawable.weather_frost
                        WeatherEffect.FOG -> R.drawable.weather_fog
                        WeatherEffect.RAIN -> R.drawable.weather_rain
                    }
                ),
                contentDescription = effect.name,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Composable
fun ScorePanel(
    gameState: GwentGameState?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Счёт противника
        ScoreSection(
            label = "Opponent",
            currentScore = if (gameState != null) {
                calculateScore(gameState.aiField, gameState.weatherEffects)
            } else 0,
            roundsWon = gameState?.aiRoundsWon ?: 0,
            isPassed = gameState?.aiPassed == true
        )

        Divider(
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Счёт игрока
        ScoreSection(
            label = "You",
            currentScore = if (gameState != null) {
                calculateScore(gameState.playerField, gameState.weatherEffects)
            } else 0,
            roundsWon = gameState?.playerRoundsWon ?: 0,
            isPassed = gameState?.playerPassed == true
        )

        // Номер текущего раунда
        Text(
            text = "Round ${gameState?.currentRound ?: 1}",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun ScoreSection(
    label: String,
    currentScore: Int,
    roundsWon: Int,
    isPassed: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = currentScore.toString(),
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            repeat(2) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = if (index < roundsWon) Color.Yellow else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }

        if (isPassed) {
            Text(
                text = "PASSED",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Вспомогательная функция для подсчета очков
private fun calculateScore(
    field: Map<GwentCardRow, List<GwentCard>>,
    weatherEffects: List<WeatherEffect>
): Int {
    return field.entries.sumOf { (row, cards) ->
        when {
            row == GwentCardRow.MELEE && WeatherEffect.FROST in weatherEffects -> 0
            row == GwentCardRow.RANGED && WeatherEffect.FOG in weatherEffects -> 0
            row == GwentCardRow.SIEGE && WeatherEffect.RAIN in weatherEffects -> 0
            else -> cards.sumOf { it.power }
        }
    }
}

@Composable
fun RoundResultDialog(
    winner: String,
    playerScore: Int,
    aiScore: Int,
    playerRoundsWon: Int,
    aiRoundsWon: Int,
    currentRound: Int,
    onContinue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = when (winner) {
                    "player" -> "Round Won!"
                    "ai" -> "Round Lost!"
                    else -> "Draw!"
                },
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Column {
                Text("Round $currentRound Score:")
                Text("You: $playerScore")
                Text("Opponent: $aiScore")
                Text("\nRounds Won:")
                Text("You: $playerRoundsWon")
                Text("Opponent: $aiRoundsWon")
            }
        },
        confirmButton = {
            Button(onClick = onContinue) {
                Text("Continue")
            }
        }
    )
}

@Composable
fun GameOverDialog(
    playerRoundsWon: Int,
    aiRoundsWon: Int,
    finalPlayerScore: Int,
    finalAiScore: Int,
    onNewGame: () -> Unit,
    onExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = if (playerRoundsWon > aiRoundsWon) "Victory!" else "Defeat!",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Column {
                Text("Final Score:")
                Text("You: $finalPlayerScore")
                Text("Opponent: $finalAiScore")
                Text("\nRounds Won:")
                Text("You: $playerRoundsWon")
                Text("Opponent: $aiRoundsWon")
            }
        },
        confirmButton = {
            Button(onClick = onNewGame) {
                Text("New Game")
            }
        },
        dismissButton = {
            Button(onClick = onExit) {
                Text("Exit")
            }
        }
    )
} 