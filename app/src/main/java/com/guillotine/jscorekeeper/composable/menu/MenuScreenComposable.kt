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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guillotine.jscorekeeper.FinalScreen
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.PastGamesListScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.data.SavedGame
import com.guillotine.jscorekeeper.viewmodels.MenuScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenComposable(navController: NavHostController, savedGame: SavedGame? = null) {
    val viewModel = MenuScreenViewModel()
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
                    // Make the column only as wide as the widest button.
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.isSavedGame(savedGame)) {
                    Button (
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (savedGame != null && savedGame.isFinal) {
                                navController.navigate(
                                    FinalScreen(
                                        score = savedGame.score,
                                        round = savedGame.round,
                                        currency = savedGame.gameData.currency
                                    )
                                )
                            } else {
                                navController.navigate(
                                    GameScreen(
                                        gameMode = GameModes.RESUME
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

                        navController.navigate(
                            GameScreen(
                                gameMode = GameModes.USA
                            )
                        )

                    }
                ) {
                    Text(stringResource(R.string.play_us_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(
                            GameScreen(
                                gameMode = GameModes.UK
                            )
                        )

                    }
                ) {
                    Text(stringResource(R.string.play_uk_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(
                            GameScreen(
                                gameMode = GameModes.AUSTRALIA
                            )
                        )

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

@Preview
@Composable
fun MenuPreview() {
    MenuScreenComposable(rememberNavController())
}