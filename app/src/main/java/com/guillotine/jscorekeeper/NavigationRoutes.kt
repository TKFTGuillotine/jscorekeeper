package com.guillotine.jscorekeeper

import com.guillotine.jscorekeeper.data.GameModes
import kotlinx.serialization.Serializable

@Serializable
object MenuScreen

@Serializable
data class GameScreen(
    val gameMode: GameModes
)

@Serializable
data class ResultsScreen (
    val score: Int,
)

@Serializable
object PastGamesListScreen

// Might not bother with this but it's an idea.
@Serializable
object RulesScreen