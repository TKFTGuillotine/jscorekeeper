package com.guillotine.jscorekeeper

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.datastore.dataStore
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.guillotine.jscorekeeper.composable.game.GameScreenComposable
import com.guillotine.jscorekeeper.composable.menu.MenuScreenComposable
import com.guillotine.jscorekeeper.data.GameData
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.data.SavedGameSerializer
import com.guillotine.jscorekeeper.ui.theme.JScorekeeperTheme
import com.guillotine.jscorekeeper.viewmodels.GameScreenViewModel

class MainActivity : ComponentActivity() {
    private lateinit var gameScreenViewModel: GameScreenViewModel
    private val Context.dataStore by dataStore("saved_game.json", SavedGameSerializer)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JScorekeeperTheme {
                val savedGame = dataStore.data.collectAsState(
                    initial = null
                )
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = MenuScreen
                ) {
                    composable<MenuScreen> {
                        MenuScreenComposable(navController, savedGame.value)
                    }
                    composable<GameScreen> { navBackStackEntry ->
                        val route: GameScreen = navBackStackEntry.toRoute<GameScreen>()
                        val gameMode = route.gameMode
                        val extras = MutableCreationExtras().apply {
                            set(
                                GameScreenViewModel.GAME_DATA_KEY,
                                processGameData(applicationContext, gameMode)
                            )
                            set(GameScreenViewModel.DATA_STORE_KEY, dataStore)
                            set(SAVED_STATE_REGISTRY_OWNER_KEY, navBackStackEntry)
                            set(VIEW_MODEL_STORE_OWNER_KEY, navBackStackEntry)
                        }
                        gameScreenViewModel = viewModel(
                            factory = GameScreenViewModel.Factory,
                            extras = extras,
                        )
                        GameScreenComposable(navController, gameScreenViewModel)
                    }
                    composable<ResultsScreen> {
                        Text("Result: ${it.arguments?.getInt("score")}")
                    }
                    composable<PastGamesListScreen> {
                        Text("History Screen!")
                    }
                }
            }
        }
    }

    private fun processGameData(applicationContext: Context, gameMode: GameModes): GameData {
        val rounds = when (gameMode) {
            GameModes.USA -> applicationContext.resources.getIntArray(R.array.usa_round_multiplier)
            GameModes.UK -> applicationContext.resources.getIntArray(R.array.uk_round_multiplier)
            GameModes.AUSTRALIA -> applicationContext.resources.getIntArray(R.array.australia_round_multiplier)
            GameModes.RESUME -> applicationContext.resources.getIntArray(R.array.resume_round_multiplier)
        }
        val columns = when (gameMode) {
            GameModes.USA -> applicationContext.resources.getInteger(R.integer.usa_columns)
            GameModes.UK -> applicationContext.resources.getInteger(R.integer.uk_columns)
            GameModes.AUSTRALIA -> applicationContext.resources.getInteger(R.integer.australia_columns)
            GameModes.RESUME -> applicationContext.resources.getInteger(R.integer.resume_columns)
        }
        val moneyValues = when (gameMode) {
            GameModes.USA -> applicationContext.resources.getIntArray(R.array.usa_money)
            GameModes.UK -> applicationContext.resources.getIntArray(R.array.uk_money)
            GameModes.AUSTRALIA -> applicationContext.resources.getIntArray(R.array.australia_money)
            GameModes.RESUME -> applicationContext.resources.getIntArray(R.array.resume_money)
        }
        val currency = when (gameMode) {
            GameModes.USA -> applicationContext.resources.getString(R.string.usa_currency)
            GameModes.UK -> applicationContext.resources.getString(R.string.uk_currency)
            GameModes.AUSTRALIA -> applicationContext.resources.getString(R.string.australia_currency)
            GameModes.RESUME -> applicationContext.resources.getString(R.string.resume_currency)
        }

        return GameData(moneyValues, rounds, currency, columns)
    }
}