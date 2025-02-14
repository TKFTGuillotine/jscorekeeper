package com.guillotine.jscorekeeper

import com.guillotine.jscorekeeper.data.GameModes
import kotlinx.serialization.Serializable

@Serializable
object MenuScreen

@Serializable
data class GameScreen(
    val gameMode: GameModes,
    val timestamp: Long
)

@Serializable
data class FinalScreen(
    val score: Int,
    val round: Int,
    val currency: String,
    val moneyValues: IntArray,
    val multipliers: IntArray,
    val columns: Int,
    val timestamp: Long
)

@Serializable
data class ResultsScreen (
    val timestamp: Long,
    val moneyValues: IntArray,
    val multipliers: IntArray,
    val currency: String,
    val score: Int,
    val columns: Int,
    val deleteCurrentSavedGame: Boolean
)

@Serializable
object PastGamesListScreen

// Might not bother with this but it's an idea.
@Serializable
object RulesScreen