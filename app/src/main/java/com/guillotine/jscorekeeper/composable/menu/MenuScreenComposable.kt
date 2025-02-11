package com.guillotine.jscorekeeper.composable.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.guillotine.jscorekeeper.FinalScreen
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.PastGamesListScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.data.SavedGame
import com.guillotine.jscorekeeper.viewmodels.MenuScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenComposable(
    navController: NavHostController,
    savedGame: SavedGame? = null,
    viewModel: MenuScreenViewModel
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel.attemptedLoadTimestamp) {
        if (!viewModel.attemptedLoadTimestamp) {
            viewModel.loadTimestamp()
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text(stringResource(R.string.app_name))
            }
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    // Make the column only as wide as the widest button.
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // If the savedGame passed in by the Activity has 0 columns (meaning it's saved).
                if (viewModel.isSavedGame(savedGame)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (savedGame != null && savedGame.isFinal) {
                                navController.navigate(
                                    FinalScreen(
                                        score = savedGame.score,
                                        round = savedGame.round,
                                        currency = savedGame.gameData.currency,
                                        moneyValues = savedGame.gameData.moneyValues,
                                        multipliers = savedGame.gameData.multipliers,
                                        columns = savedGame.gameData.columns,
                                        timestamp = viewModel.timestamp
                                    )
                                )
                            } else {
                                navController.navigate(
                                    GameScreen(
                                        gameMode = GameModes.RESUME,
                                        // The important bit that won't get overwritten.
                                        timestamp = viewModel.timestamp
                                    )
                                )
                            }
                        }
                    ) {
                        Text(stringResource(R.string.resume_game))
                    }
                }
                Button(
                    // For each button, take up the max size of the column, such that the buttons
                    // match the size of the largest button.
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            viewModel.deleteSavedGame()
                            viewModel.createGame(GameModes.USA)
                            navController.navigate(
                                GameScreen(
                                    gameMode = GameModes.USA,
                                    timestamp = viewModel.timestamp
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.play_us_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            viewModel.deleteSavedGame()
                            viewModel.createGame(GameModes.UK)
                            navController.navigate(
                                GameScreen(
                                    gameMode = GameModes.UK,
                                    timestamp = viewModel.timestamp
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.play_uk_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            viewModel.deleteSavedGame()
                            viewModel.createGame(GameModes.AUSTRALIA)
                            navController.navigate(
                                GameScreen(
                                    gameMode = GameModes.AUSTRALIA,
                                    timestamp = viewModel.timestamp
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.play_au_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(
                            PastGamesListScreen
                        )
                    }
                ) {
                    Text(stringResource(R.string.view_history))
                }
            }
        }
    }
}