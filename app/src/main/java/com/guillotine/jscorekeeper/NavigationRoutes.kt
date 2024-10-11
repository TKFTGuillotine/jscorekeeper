package com.guillotine.jscorekeeper

import kotlinx.serialization.Serializable

@Serializable
object MenuScreen

@Serializable
data class GameScreen(
    val rounds: Int,
    val columns: Int,
    val moneyValues: IntArray,
    val currency: String,
    val isResumeGame: Boolean
)

@Serializable
object ResultsScreen

@Serializable
object PastGamesListScreen

// Might not bother with this but it's an idea.
@Serializable
object RulesScreen