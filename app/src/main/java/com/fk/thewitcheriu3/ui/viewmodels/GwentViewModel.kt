package com.fk.thewitcheriu3.ui.viewmodels

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.gwent.*

@Stable
class GwentViewModel : ViewModel() {
    var gameState by mutableStateOf<GwentGameState?>(null)

    var showRoundResult by mutableStateOf(false)
        private set

    private val defaultDeck = listOf(
        GwentCard(1, "Geralt", 15, GwentCardRow.MELEE, R.drawable.gwent_card_geralt, isHero = true),
        GwentCard(2, "Vernon Roche", 10, GwentCardRow.MELEE, R.drawable.gwent_card_vernon_roche, isHero = true),
        GwentCard(3, "Siegfried Of Denesle", 5, GwentCardRow.MELEE, R.drawable.gwent_card_siegfried_of_denesle),
        GwentCard(4, "Yarpen Zigrin", 2, GwentCardRow.MELEE, R.drawable.gwent_card_yarpen_zigrin),
        GwentCard(5, "Sabrina Glevissig", 4, GwentCardRow.RANGED, R.drawable.gwent_card_sabrina_glevissig),
        GwentCard(6, "Dethmold", 6, GwentCardRow.RANGED, R.drawable.gwent_card_dethmold),
        GwentCard(7, "Keira Metz", 5, GwentCardRow.RANGED, R.drawable.gwent_card_keira_metz),
        GwentCard(8, "Ballista", 6, GwentCardRow.SIEGE, R.drawable.gwent_card_ballista),
        GwentCard(9, "Kaedweni Siege Expert", 1, GwentCardRow.SIEGE, R.drawable.gwent_card_kaedweni_siege_expert),
        GwentCard(10, "Catapult", 8, GwentCardRow.SIEGE, R.drawable.gwent_card_catapult),
        GwentCard(11, "Frost", 0, GwentCardRow.WEATHER, R.drawable.weather_frost, weatherEffect = WeatherEffect.FROST),
        GwentCard(12, "Fog", 0, GwentCardRow.WEATHER, R.drawable.weather_fog, weatherEffect = WeatherEffect.FOG),
        GwentCard(13, "Rain", 0, GwentCardRow.WEATHER, R.drawable.weather_rain, weatherEffect = WeatherEffect.RAIN),
    )

    init {
        startNewGame()
    }

    fun startNewGame() {
        val shuffledDeck = defaultDeck.shuffled()
        val playerHand = shuffledDeck.take(10)
        val aiHand = shuffledDeck.drop(10).take(10)

        gameState = GwentGameState(
            playerHand = playerHand,
            aiHand = aiHand,
            playerField = GwentCardRow.entries.associateWith { emptyList() },
            aiField = GwentCardRow.entries.associateWith { emptyList() },
            weatherEffects = emptyList()
        )
    }

    private fun checkRoundEnd(state: GwentGameState): GwentGameState {
        if ((state.playerPassed && state.aiPassed) || 
            (state.playerHand.isEmpty() && state.aiHand.isEmpty())) {
            return finishRound(state)
        }
        return state
    }

    private fun finishRound(state: GwentGameState): GwentGameState {
        val playerScore = calculateScore(state.playerField, state.weatherEffects)
        val aiScore = calculateScore(state.aiField, state.weatherEffects)

        if (state.playerPassed && state.aiPassed && 
            state.playerField.all { it.value.isEmpty() } && 
            state.aiField.all { it.value.isEmpty() }) {
            return startNewRound(state)
        }

        if (playerScore == aiScore) {
            return startNewRound(state)
        }

        val (newPlayerRoundsWon, newAiRoundsWon) = when {
            playerScore > aiScore -> Pair(state.playerRoundsWon + 1, state.aiRoundsWon)
            aiScore > playerScore -> Pair(state.playerRoundsWon, state.aiRoundsWon + 1)
            else -> Pair(state.playerRoundsWon, state.aiRoundsWon)
        }

        showRoundResult = true

        if (newPlayerRoundsWon >= 2 || newAiRoundsWon >= 2) {
            return state.copy(
                playerRoundsWon = newPlayerRoundsWon,
                aiRoundsWon = newAiRoundsWon,
                isGameOver = true,
                roundWinner = if (playerScore > aiScore) "player" else "ai",
                finalPlayerScore = playerScore,
                finalAiScore = aiScore
            )
        }

        return state.copy(
            playerField = GwentCardRow.entries.associateWith { emptyList() },
            aiField = GwentCardRow.entries.associateWith { emptyList() },
            weatherEffects = emptyList(),
            playerPassed = false,
            aiPassed = false,
            currentRound = state.currentRound + 1,
            playerRoundsWon = newPlayerRoundsWon,
            aiRoundsWon = newAiRoundsWon,
            isPlayerTurn = true,
            roundWinner = if (playerScore > aiScore) "player" else "ai",
            lastRoundPlayerScore = playerScore,
            lastRoundAiScore = aiScore
        )
    }

    private fun startNewRound(state: GwentGameState): GwentGameState {
        return state.copy(
            playerField = GwentCardRow.entries.associateWith { emptyList() },
            aiField = GwentCardRow.entries.associateWith { emptyList() },
            weatherEffects = emptyList(),
            playerPassed = false,
            aiPassed = false,
            isPlayerTurn = true
        )
    }

    fun playCard(card: GwentCard) {
        val currentState = gameState ?: return
        if (!currentState.isPlayerTurn || currentState.playerPassed) return

        val newState = when (card.row) {
            GwentCardRow.WEATHER -> applyWeatherCard(card, currentState)
            else -> playNormalCard(card, currentState)
        }

        var updatedState = checkRoundEnd(newState)

        if (!updatedState.isGameOver && !updatedState.aiPassed && updatedState == newState) {
            updatedState = makeAiMove(updatedState)
            updatedState = checkRoundEnd(updatedState)
        }

        gameState = updatedState
    }

    private fun playNormalCard(card: GwentCard, state: GwentGameState): GwentGameState {
        val newPlayerHand = state.playerHand - card
        val newPlayerField = state.playerField.toMutableMap()
        val currentRow = newPlayerField[card.row] ?: emptyList()
        newPlayerField[card.row] = currentRow + card

        return state.copy(
            playerHand = newPlayerHand,
            playerField = newPlayerField,
            isPlayerTurn = false
        )
    }

    private fun applyWeatherCard(card: GwentCard, state: GwentGameState): GwentGameState {
        val weatherEffect = card.weatherEffect ?: return state
        val newPlayerHand = state.playerHand - card
        val newWeatherEffects = state.weatherEffects + weatherEffect

        return state.copy(
            playerHand = newPlayerHand,
            weatherEffects = newWeatherEffects,
            isPlayerTurn = false
        )
    }

    private fun makeAiMove(state: GwentGameState): GwentGameState {
        if (state.aiPassed) return state

        val playerScore = calculateScore(state.playerField, state.weatherEffects)
        val aiScore = calculateScore(state.aiField, state.weatherEffects)

        if (shouldAiPass(state, playerScore, aiScore)) {
            return state.copy(
                aiPassed = true,
                isPlayerTurn = true
            ).let { checkRoundEnd(it) }
        }

        val aiCard = selectAiCard(state)
        if (aiCard == null) {
            return state.copy(
                aiPassed = true,
                isPlayerTurn = true
            ).let { checkRoundEnd(it) }
        }

        return when (aiCard.row) {
            GwentCardRow.WEATHER -> applyAiWeatherCard(aiCard, state)
            else -> playAiNormalCard(aiCard, state)
        }.copy(isPlayerTurn = true)
    }

    private fun selectAiCard(state: GwentGameState): GwentCard? {
        if (state.aiHand.isEmpty()) return null
        
        val playerScore = calculateScore(state.playerField, state.weatherEffects)
        val aiScore = calculateScore(state.aiField, state.weatherEffects)
        val scoreDiff = playerScore - aiScore

        return when {
            scoreDiff > 0 -> state.aiHand.maxByOrNull { it.power }
            
            scoreDiff < -10 -> state.aiHand.minByOrNull { it.power }

            else -> state.aiHand.sortedBy { it.power }
                .getOrNull(state.aiHand.size / 2)
        }
    }

    internal fun calculateScore(field: Map<GwentCardRow, List<GwentCard>>, weatherEffects: List<WeatherEffect>): Int {
        return field.entries.sumOf { (row, cards) ->
            when {
                row == GwentCardRow.MELEE && WeatherEffect.FROST in weatherEffects -> 0
                row == GwentCardRow.RANGED && WeatherEffect.FOG in weatherEffects -> 0
                row == GwentCardRow.SIEGE && WeatherEffect.RAIN in weatherEffects -> 0
                else -> cards.sumOf { it.power }
            }
        }
    }

    private fun playAiNormalCard(card: GwentCard, state: GwentGameState): GwentGameState {
        val newAiHand = state.aiHand - card
        val newAiField = state.aiField.toMutableMap()
        val currentRow = newAiField[card.row] ?: emptyList()
        newAiField[card.row] = currentRow + card

        return state.copy(
            aiHand = newAiHand,
            aiField = newAiField
        )
    }

    private fun applyAiWeatherCard(card: GwentCard, state: GwentGameState): GwentGameState {
        val weatherEffect = card.weatherEffect ?: return state
        val newAiHand = state.aiHand - card
        val newWeatherEffects = state.weatherEffects + weatherEffect

        return state.copy(
            aiHand = newAiHand,
            weatherEffects = newWeatherEffects
        )
    }

    fun pass() {
        val currentState = gameState ?: return
        if (currentState.playerPassed) return

        var newState = currentState.copy(
            playerPassed = true,
            isPlayerTurn = false
        )

        if (newState.aiPassed) {
            newState = checkRoundEnd(newState)
        } else {
            newState = makeAiMove(newState)
            newState = checkRoundEnd(newState)
        }

        gameState = newState
    }

    internal fun shouldAiPass(state: GwentGameState, playerScore: Int, aiScore: Int): Boolean {
        return when {
            // Если игрок спасовал и у AI больше очков - пасуем
            state.playerPassed && aiScore > playerScore -> true
            
            // Если у AI большое преимущество - пасуем
            aiScore > playerScore + 15 -> true
            
            // Если осталось мало карт и есть преимущество - пасуем
            state.aiHand.size <= 2 && aiScore > playerScore -> true
            
            // Если суммарная сила оставшихся карт не позволит догнать игрока - пасуем
            state.aiHand.sumOf { it.power } + aiScore < playerScore -> true
            
            else -> false
        }
    }

    fun continueGame() {
        showRoundResult = false
    }
} 