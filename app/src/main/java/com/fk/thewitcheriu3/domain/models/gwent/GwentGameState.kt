package com.fk.thewitcheriu3.domain.models.gwent

data class GwentGameState(
    val playerHand: List<GwentCard>,
    val aiHand: List<GwentCard>,
    val playerField: Map<GwentCardRow, List<GwentCard>>,
    val aiField: Map<GwentCardRow, List<GwentCard>>,
    val weatherEffects: List<WeatherEffect>,
    val currentRound: Int = 1,
    val playerRoundsWon: Int = 0,
    val aiRoundsWon: Int = 0,
    val playerPassed: Boolean = false,
    val aiPassed: Boolean = false,
    val isPlayerTurn: Boolean = true,
    val isGameOver: Boolean = false,
    val roundWinner: String? = null,
    val lastRoundPlayerScore: Int = 0,
    val lastRoundAiScore: Int = 0,
    val finalPlayerScore: Int = 0,
    val finalAiScore: Int = 0
) 