package com.guillotine.jscorekeeper

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.TransformOrigin
import androidx.datastore.dataStore
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.guillotine.jscorekeeper.database.StatisticsDatabase
import com.guillotine.jscorekeeper.composable.finalj.FinalScreenComposable
import com.guillotine.jscorekeeper.composable.game.GameScreenComposable
import com.guillotine.jscorekeeper.composable.menu.MenuScreenComposable
import com.guillotine.jscorekeeper.composable.results.ResultsScreenComposable
import com.guillotine.jscorekeeper.data.GameData
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.data.SavedGame
import com.guillotine.jscorekeeper.data.SavedGameSerializer
import com.guillotine.jscorekeeper.ui.theme.JScorekeeperTheme
import com.guillotine.jscorekeeper.viewmodels.FinalScreenViewModel
import com.guillotine.jscorekeeper.viewmodels.GameScreenViewModel
import com.guillotine.jscorekeeper.viewmodels.MenuScreenViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var gameScreenViewModel: GameScreenViewModel
    private lateinit var finalScreenViewModel: FinalScreenViewModel
    private lateinit var menuScreenViewModel: MenuScreenViewModel

    private val Context.dataStore by dataStore("saved_game.json", SavedGameSerializer)
    private lateinit var statisticsDatabase: StatisticsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statisticsDatabase = Room.databaseBuilder(
            applicationContext,
            StatisticsDatabase::class.java,
            "statistics_database"
        ).build()
        enableEdgeToEdge()
        setContent {
            JScorekeeperTheme {
                val savedGame = dataStore.data.collectAsState(
                    initial = null
                )
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = MenuScreen,
                    /* Despite my best efforts, it seems as though there's no real good way to
                       implement the stock Android Predictive Back animation in Compose. Due to the
                       way this works, the user has the ability to complete the animation before
                       lifting their finger to complete the back action, rather than the animation
                       being at a maximum halfway complete as it is in the stock configuration.

                       With that in mind, that it is effectively impossible to implement the stock
                       configuration, I'm using Google's own suggested animation combinations
                       here instead, straight from the official Google docs.
                    */
                    popExitTransition = {
                        scaleOut(
                            targetScale = 0.9f,
                            transformOrigin = TransformOrigin.Center
                        ) + fadeOut(
                            animationSpec = tween(
                                easing = CubicBezierEasing(0.1f, 0.1f, 0f, 1f),
                            )
                        )
                    },
                    popEnterTransition = {
                        scaleIn(
                            initialScale = 1.1F,
                        ) // Fade in from the official docs removed to avoid background flash.
                    },
                    // This one is just a guess.
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    },
                    exitTransition = { ExitTransition.None },
                ) {
                    composable<MenuScreen> { navBackStackEntry ->
                        val extras = MutableCreationExtras().apply {
                            set(MenuScreenViewModel.STATISTICS_DATABASE_KEY, statisticsDatabase)
                            set(SAVED_STATE_REGISTRY_OWNER_KEY, navBackStackEntry)
                            set(VIEW_MODEL_STORE_OWNER_KEY, navBackStackEntry)
                        }
                        menuScreenViewModel = viewModel(
                            factory = MenuScreenViewModel.Factory,
                            extras = extras
                        )
                        MenuScreenComposable(
                            navController,
                            savedGame.value,
                            menuScreenViewModel
                        )
                    }
                    composable<GameScreen> { navBackStackEntry ->
                        val route: GameScreen = navBackStackEntry.toRoute<GameScreen>()
                        val gameMode = route.gameMode
                        val gameData = processGameData(applicationContext, gameMode)
                        if (gameMode != GameModes.RESUME) {
                            // Data has already been deleted from Room by the menu screen, but to
                            // simplify things, we'll write the new game to the DataStore here.
                            lifecycleScope.launch {
                                writeNewSavedGame(gameData)
                            }
                        }
                        val extras = MutableCreationExtras().apply {
                            set(
                                GameScreenViewModel.GAME_DATA_KEY,
                                gameData
                            )
                            set(GameScreenViewModel.DATA_STORE_KEY, dataStore)
                            set(GameScreenViewModel.GAME_TIMESTAMP_KEY, route.timestamp)
                            set(SAVED_STATE_REGISTRY_OWNER_KEY, navBackStackEntry)
                            set(VIEW_MODEL_STORE_OWNER_KEY, navBackStackEntry)
                            set(GameScreenViewModel.STATISTICS_DATABASE_KEY, statisticsDatabase)
                        }
                        gameScreenViewModel = viewModel(
                            factory = GameScreenViewModel.Factory,
                            extras = extras,
                        )
                        GameScreenComposable(navController, gameScreenViewModel)
                    }
                    composable<FinalScreen> { navBackStackEntry ->
                        val route: FinalScreen = navBackStackEntry.toRoute<FinalScreen>()
                        val extras = MutableCreationExtras().apply {
                            set(
                                FinalScreenViewModel.STATISTICS_DATABASE_KEY,
                                statisticsDatabase
                            )
                            set(FinalScreenViewModel.GAME_TIMESTAMP_KEY, route.timestamp)
                            set(SAVED_STATE_REGISTRY_OWNER_KEY, navBackStackEntry)
                            set(VIEW_MODEL_STORE_OWNER_KEY, navBackStackEntry)
                        }
                        finalScreenViewModel = viewModel(
                            factory = FinalScreenViewModel.Factory,
                            extras = extras,
                        )
                        FinalScreenComposable(navController, finalScreenViewModel, route)
                    }
                    composable<ResultsScreen> { navBackStackEntry ->
                        val route = navBackStackEntry.toRoute<ResultsScreen>()
                        if (route.deleteCurrentSavedGame) {
                            lifecycleScope.launch {
                                deleteSavedGame()
                            }
                        }

                        ResultsScreenComposable(navController, route)
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

    private suspend fun writeNewSavedGame(gameData: GameData) {
        val defaultValues = SavedGame(
            gameData = gameData,
            savedMoneyValues = gameData.moneyValues,
            score = 0,
            round = 0,
            columnsPerValue = mutableMapOf<Int, Int>().toMutableMap(),
            remainingDailyDoubles = gameData.multipliers[0],
            isFinal = false,
            clueIndex = 0
        )
        dataStore.updateData {
            it.copy(
                gameData = defaultValues.gameData,
                savedMoneyValues = defaultValues.savedMoneyValues,
                score = defaultValues.score,
                round = defaultValues.round,
                columnsPerValue = defaultValues.columnsPerValue,
                remainingDailyDoubles = defaultValues.remainingDailyDoubles,
                isFinal = defaultValues.isFinal,
            )
        }
    }

    private suspend fun deleteSavedGame() {
        val defaultValues = SavedGame(
            gameData = GameData(
                moneyValues = intArrayOf(0),
                multipliers = intArrayOf(0),
                currency = "",
                // This will never be the case in a real game, so this being a default value
                // indicates there is no saved game to the menu screen.
                columns = 0,
            ),
            savedMoneyValues = intArrayOf(0),
            score = 0,
            round = 0,
            columnsPerValue = mutableMapOf<Int, Int>().toMutableMap(),
            remainingDailyDoubles = 0,
            isFinal = false,
            clueIndex = 0
        )
        dataStore.updateData {
            it.copy(
                gameData = defaultValues.gameData,
                savedMoneyValues = defaultValues.savedMoneyValues,
                score = defaultValues.score,
                round = defaultValues.round,
                columnsPerValue = defaultValues.columnsPerValue,
                remainingDailyDoubles = defaultValues.remainingDailyDoubles,
                isFinal = defaultValues.isFinal,
            )
        }
    }
}