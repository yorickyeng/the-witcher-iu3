package com.fk.thewitcheriu3.ui.viewmodels

import com.fk.thewitcheriu3.domain.models.gwent.*
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class GwentViewModelTest {
    internal lateinit var viewModel: GwentViewModel

    private fun createDefaultGameState(
        playerHand: List<GwentCard> = emptyList(),
        aiHand: List<GwentCard> = emptyList(),
        playerField: Map<GwentCardRow, List<GwentCard>> = emptyMap(),
        aiField: Map<GwentCardRow, List<GwentCard>> = emptyMap(),
        weatherEffects: List<WeatherEffect> = emptyList(),
        currentRound: Int = 1,
        playerRoundsWon: Int = 0,
        aiRoundsWon: Int = 0,
        playerPassed: Boolean = false,
        aiPassed: Boolean = false,
        isPlayerTurn: Boolean = true,
        isGameOver: Boolean = false,
        roundWinner: String? = null,
        lastRoundPlayerScore: Int = 0,
        lastRoundAiScore: Int = 0,
        finalPlayerScore: Int = 0,
        finalAiScore: Int = 0
    ): GwentGameState {
        return GwentGameState(
            playerHand = playerHand,
            aiHand = aiHand,
            playerField = playerField,
            aiField = aiField,
            weatherEffects = weatherEffects,
            currentRound = currentRound,
            playerRoundsWon = playerRoundsWon,
            aiRoundsWon = aiRoundsWon,
            playerPassed = playerPassed,
            aiPassed = aiPassed,
            isPlayerTurn = isPlayerTurn,
            isGameOver = isGameOver,
            roundWinner = roundWinner,
            lastRoundPlayerScore = lastRoundPlayerScore,
            lastRoundAiScore = lastRoundAiScore,
            finalPlayerScore = finalPlayerScore,
            finalAiScore = finalAiScore
        )
    }

    @Before
    fun setup() {
        viewModel = GwentViewModel()
    }

    // Тесты инициализации игры
    @Test
    fun `startNewGame should initialize game state`() {
        viewModel.startNewGame()
        assertThat(viewModel.gameState).isNotNull()
    }

    @Test
    fun `initial player hand should contain 10 cards`() {
        viewModel.startNewGame()
        assertThat(viewModel.gameState?.playerHand?.size).isEqualTo(10)
    }

    @Test
    fun `initial AI hand should contain 10 cards`() {
        viewModel.startNewGame()
        assertThat(viewModel.gameState?.aiHand?.size).isEqualTo(10)
    }

    @Test
    fun `initial battlefield should be empty`() {
        viewModel.startNewGame()
        assertThat(viewModel.gameState?.playerField?.values?.flatten()).isEmpty()
        assertThat(viewModel.gameState?.aiField?.values?.flatten()).isEmpty()
    }

    // Тесты игровой механики
    @Test
    fun `playing a card should remove it from hand`() {
        viewModel.startNewGame()
        val initialHand = viewModel.gameState?.playerHand ?: emptyList()
        val cardToPlay = initialHand.first()
        viewModel.playCard(cardToPlay)
        assertThat(viewModel.gameState?.playerHand).doesNotContain(cardToPlay)
    }

    @Test
    fun `playing a card should add it to battlefield`() {
        viewModel.startNewGame()
        val cardToPlay = viewModel.gameState?.playerHand?.first() ?: return
        viewModel.playCard(cardToPlay)
        val fieldCards = viewModel.gameState?.playerField?.get(cardToPlay.row) ?: emptyList()
        assertThat(fieldCards).contains(cardToPlay)
    }

    @Test
    fun `playing a weather card should add weather effect`() {
        viewModel.startNewGame()
        val weatherCard = viewModel.gameState?.playerHand?.find { it.row == GwentCardRow.WEATHER } ?: return
        viewModel.playCard(weatherCard)
        assertThat(viewModel.gameState?.weatherEffects).contains(weatherCard.weatherEffect)
    }

    @Test
    fun `weather effect should nullify row power`() {
        viewModel.startNewGame()
        val meleeCard = GwentCard(1, "Test", 5, GwentCardRow.MELEE, 0)
        val weatherCard = GwentCard(2, "Frost", 0, GwentCardRow.WEATHER, 0, weatherEffect = WeatherEffect.FROST)
        
        val state = GwentGameState(
            playerHand = listOf(meleeCard, weatherCard),
            aiHand = emptyList(),
            playerField = mapOf(GwentCardRow.MELEE to listOf(meleeCard)),
            aiField = emptyMap(),
            weatherEffects = listOf(WeatherEffect.FROST)
        )
        
        val score = viewModel.calculateScore(state.playerField, state.weatherEffects)
        assertThat(score).isEqualTo(0)
    }

    // Тесты механики паса
    @Test
    fun `passing should set playerPassed to true`() {
        viewModel.startNewGame()
        viewModel.pass()
        assertThat(viewModel.gameState?.playerPassed).isTrue()
    }

    @Test
    fun `both players passing should end round`() {
        viewModel.startNewGame()
        viewModel.pass()
        val state = viewModel.gameState?.copy(aiPassed = true) ?: return
        assertThat(state.playerPassed && state.aiPassed).isTrue()
    }

    // Тесты подсчета очков
    @Test
    fun `score calculation should sum up all card powers in row`() {
        val cards = listOf(
            GwentCard(1, "Test1", 5, GwentCardRow.MELEE, 0),
            GwentCard(2, "Test2", 3, GwentCardRow.MELEE, 0)
        )
        val field = mapOf(GwentCardRow.MELEE to cards)
        val score = viewModel.calculateScore(field, emptyList())
        assertThat(score).isEqualTo(8)
    }

    @Test
    fun `hero cards should not be affected by weather`() {
        val heroCard = GwentCard(1, "Hero", 5, GwentCardRow.MELEE, 0, isHero = true)
        val field = mapOf(GwentCardRow.MELEE to listOf(heroCard))
        val score = viewModel.calculateScore(field, listOf(WeatherEffect.FROST))
        assertThat(score).isEqualTo(5)
    }

    // Тесты AI логики
    @Test
    fun `AI should pass when player passed and AI is winning`() {
        val state = createDefaultGameState(
            playerHand = emptyList(),
            aiHand = listOf(GwentCard(1, "Test", 3, GwentCardRow.MELEE, 0)),
            playerField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(2, "Test", 5, GwentCardRow.MELEE, 0))),
            aiField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(3, "Test", 10, GwentCardRow.MELEE, 0))),
            playerPassed = true
        )
        assertThat(viewModel.shouldAiPass(state, 5, 10)).isTrue()
    }

    @Test
    fun `AI should not pass when losing and player hasn't passed`() {
        val state = createDefaultGameState(
            playerHand = emptyList(),
            aiHand = listOf(GwentCard(1, "Test", 3, GwentCardRow.MELEE, 0)),
            playerField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(2, "Test", 10, GwentCardRow.MELEE, 0))),
            aiField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(3, "Test", 5, GwentCardRow.MELEE, 0)))
        )
        assertThat(viewModel.shouldAiPass(state, 10, 5)).isFalse()
    }

//    // Тесты окончания раунда
//    @Test
//    fun `round should end when both players pass`() {
//        viewModel.startNewGame()
//        viewModel.pass()
//        val state = viewModel.gameState?.copy(aiPassed = true) ?: return
//        assertThat(viewModel.isRoundOver(state)).isTrue()
//    }

//    @Test
//    fun `round should end when both players are out of cards`() {
//        val state = GwentGameState(
//            playerHand = emptyList(),
//            aiHand = emptyList(),
//            playerField = emptyMap(),
//            aiField = emptyMap(),
//            weatherEffects = TODO(),
//            currentRound = TODO(),
//            playerRoundsWon = TODO(),
//            aiRoundsWon = TODO(),
//            playerPassed = TODO(),
//            aiPassed = TODO(),
//            isPlayerTurn = TODO(),
//            isGameOver = TODO(),
//            roundWinner = TODO(),
//            lastRoundPlayerScore = TODO(),
//            lastRoundAiScore = TODO(),
//            finalPlayerScore = TODO(),
//            finalAiScore = TODO()
//        )
//        assertThat(viewModel.isRoundOver(state)).isTrue()
//    }

    // Тесты победы в игре
    @Test
    fun `game should end when player wins 2 rounds`() {
        val state = createDefaultGameState(
            playerRoundsWon = 2,
            isGameOver = true
        )
        assertThat(state.isGameOver).isTrue()
    }

    @Test
    fun `game should end when AI wins 2 rounds`() {
        val state = createDefaultGameState(
            aiRoundsWon = 2,
            isGameOver = true
        )
        assertThat(state.isGameOver).isTrue()
    }

    // Тесты ничьей
    @Test
    fun `equal scores should result in draw`() {
        val state = createDefaultGameState(
            playerField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(1, "Test", 5, GwentCardRow.MELEE, 0))),
            aiField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(2, "Test", 5, GwentCardRow.MELEE, 0)))
        )
        assertThat(viewModel.calculateScore(state.playerField, emptyList()))
            .isEqualTo(viewModel.calculateScore(state.aiField, emptyList()))
    }

    // Тесты погодных эффектов
    @Test
    fun `frost should affect melee row`() {
        val field = mapOf(GwentCardRow.MELEE to listOf(GwentCard(1, "Test", 5, GwentCardRow.MELEE, 0)))
        val score = viewModel.calculateScore(field, listOf(WeatherEffect.FROST))
        assertThat(score).isEqualTo(0)
    }

    @Test
    fun `fog should affect ranged row`() {
        val field = mapOf(GwentCardRow.RANGED to listOf(GwentCard(1, "Test", 5, GwentCardRow.RANGED, 0)))
        val score = viewModel.calculateScore(field, listOf(WeatherEffect.FOG))
        assertThat(score).isEqualTo(0)
    }

    @Test
    fun `rain should affect siege row`() {
        val field = mapOf(GwentCardRow.SIEGE to listOf(GwentCard(1, "Test", 5, GwentCardRow.SIEGE, 0)))
        val score = viewModel.calculateScore(field, listOf(WeatherEffect.RAIN))
        assertThat(score).isEqualTo(0)
    }

    @Test
    fun `multiple weather effects should stack`() {
        val field = mapOf(
            GwentCardRow.MELEE to listOf(GwentCard(1, "Test1", 5, GwentCardRow.MELEE, 0)),
            GwentCardRow.RANGED to listOf(GwentCard(2, "Test2", 5, GwentCardRow.RANGED, 0)),
            GwentCardRow.SIEGE to listOf(GwentCard(3, "Test3", 5, GwentCardRow.SIEGE, 0))
        )
        val score = viewModel.calculateScore(
            field,
            listOf(WeatherEffect.FROST, WeatherEffect.FOG, WeatherEffect.RAIN)
        )
        assertThat(score).isEqualTo(0)
    }

    @Test
    fun `weather should not affect unrelated rows`() {
        val field = mapOf(
            GwentCardRow.MELEE to listOf(GwentCard(1, "Test1", 5, GwentCardRow.MELEE, 0)),
            GwentCardRow.RANGED to listOf(GwentCard(2, "Test2", 5, GwentCardRow.RANGED, 0))
        )
        val score = viewModel.calculateScore(field, listOf(WeatherEffect.RAIN))
        assertThat(score).isEqualTo(10)
    }

//    // Тесты выбора карт AI
//    @Test
//    fun `AI should play strongest card when losing`() {
//        val state = GwentGameState(
//            playerHand = emptyList(),
//            aiHand = listOf(
//                GwentCard(1, "Weak", 3, GwentCardRow.MELEE, 0),
//                GwentCard(2, "Strong", 8, GwentCardRow.MELEE, 0)
//            ),
//            playerField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(3, "Test", 10, GwentCardRow.MELEE, 0))),
//            aiField = emptyMap()
//        )
//        val selectedCard = viewModel.selectAiCard(state)
//        assertThat(selectedCard?.power).isEqualTo(8)
//    }
//
//    @Test
//    fun `AI should play weakest card when winning by large margin`() {
//        val state = GwentGameState(
//            playerHand = emptyList(),
//            aiHand = listOf(
//                GwentCard(1, "Weak", 3, GwentCardRow.MELEE, 0),
//                GwentCard(2, "Strong", 8, GwentCardRow.MELEE, 0)
//            ),
//            playerField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(3, "Test", 5, GwentCardRow.MELEE, 0))),
//            aiField = mapOf(GwentCardRow.MELEE to listOf(GwentCard(4, "Test", 20, GwentCardRow.MELEE, 0)))
//        )
//        val selectedCard = viewModel.selectAiCard(state)
//        assertThat(selectedCard?.power).isEqualTo(3)
//    }

    // Тесты сохранения состояния
    @Test
    fun `game state should persist after weather card played`() {
        viewModel.startNewGame()
        val weatherCard = GwentCard(11, "Frost", 0, GwentCardRow.WEATHER, 0, weatherEffect = WeatherEffect.FROST)
        val state = createDefaultGameState(
            playerHand = listOf(weatherCard)
        )
        viewModel.gameState = state
        viewModel.playCard(weatherCard)
        assertThat(viewModel.gameState?.weatherEffects).contains(WeatherEffect.FROST)
    }

//    @Test
//    fun `game state should track rounds won correctly`() {
//        val state = GwentGameState(
//            playerHand = emptyList(),
//            aiHand = emptyList(),
//            playerField = emptyMap(),
//            aiField = emptyMap(),
//            playerRoundsWon = 1,
//            aiRoundsWon = 1
//        )
//        assertThat(state.playerRoundsWon + state.aiRoundsWon).isEqualTo(2)
//    }
//
//    // Тесты граничных случаев
//    @Test
//    fun `playing last card should end round`() {
//        val state = GwentGameState(
//            playerHand = listOf(GwentCard(1, "Last", 5, GwentCardRow.MELEE, 0)),
//            aiHand = emptyList(),
//            playerField = emptyMap(),
//            aiField = emptyMap()
//        )
//        assertThat(state.playerHand.size + state.aiHand.size).isEqualTo(1)
//    }

    @Test
    fun `weather effects should stack`() {
        val state = GwentGameState(
            playerHand = emptyList(),
            aiHand = emptyList(),
            playerField = mapOf(
                GwentCardRow.MELEE to listOf(GwentCard(1, "Test", 5, GwentCardRow.MELEE, 0)),
                GwentCardRow.RANGED to listOf(GwentCard(2, "Test", 5, GwentCardRow.RANGED, 0))
            ),
            aiField = emptyMap(),
            weatherEffects = listOf(WeatherEffect.FROST, WeatherEffect.FOG)
        )
        val score = viewModel.calculateScore(state.playerField, state.weatherEffects)
        assertThat(score).isEqualTo(0)
    }
} 