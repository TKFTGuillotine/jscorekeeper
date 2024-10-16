package com.guillotine.jscorekeeper.composable.game

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guillotine.jscorekeeper.GameData
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.viewmodels.GameScreenViewModel
import com.guillotine.jscorekeeper.GameModes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenComposable(navController: NavHostController, gameScreenArgs: GameScreen) {
    val gameMode = gameScreenArgs.gameMode
    val isResumeGame = gameScreenArgs.isResumeGame
    val applicationContext = LocalContext.current.applicationContext

    val viewModel = viewModel {
        // It feels wrong to me handling this resource data here, but Google themselves recommend
        // moving any Context access up to the UI layer. Nonetheless, I have it pulled out into a
        // different function and not *stored* anywhere here such that I don't accidentally refer to
        // the wrong data later.
        GameScreenViewModel(
            processGameData(applicationContext as Application, gameMode),
            isResumeGame
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        "${
                            if (viewModel.isDoubleJ()) {
                                stringResource(R.string.double_j)
                            } else {
                                stringResource(R.string.j)
                            }
                        } - ${stringResource(R.string.round)} ${viewModel.round + 1}"
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->

        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> GameBoardHorizontalComposable(
                innerPadding = innerPadding,
                moneyValues = viewModel.moneyValues,
                currency = viewModel.currency,
                score = viewModel.score,
                onClueClick = { viewModel.showClueDialog(it) },
                //onClueClick = { viewModel.changeScore(it) },
                onNextRoundClick = { viewModel.showRoundDialog() }
            )

            else -> GameBoardVerticalComposable(
                innerPadding = innerPadding,
                moneyValues = viewModel.moneyValues,
                currency = viewModel.currency,
                score = viewModel.score,
                onClueClick = { viewModel.showClueDialog(it) },
                //onClueClick = { viewModel.changeScore(it) },
                onNextRoundClick = { viewModel.showRoundDialog() }
            )
        }

        if (viewModel.isShowRoundDialog) {
            NextRoundDialog(
                onDismissRequest = {
                    viewModel.onRoundDialogDismiss()
                },
                onConfirmation = {
                    viewModel.nextRound()
                }
            )
        } else if (viewModel.isShowClueDialog) {
            ClueDialog(
                onDismissRequest = {
                    viewModel.onClueDialogDismiss()
                },
                value = viewModel.currentValue,
                currency = viewModel.currency,
                onCorrect = {
                    viewModel.correctResponse(it)
                },
                onIncorrect = {
                    viewModel.incorrectResponse(it)
                },
                onDailyDouble = {
                    viewModel.dailyDouble(it)
                },
                onPass = {
                    viewModel.noAnswer(it)
                }
            )
        }
    }
}

fun processGameData(applicationContext: Application, gameMode: GameModes): GameData {
    val rounds = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getInteger(R.integer.usa_rounds)
        GameModes.UK -> applicationContext.resources.getInteger(R.integer.uk_rounds)
        GameModes.AUSTRALIA -> applicationContext.resources.getInteger(R.integer.australia_rounds)
    }
    val columns = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getInteger(R.integer.usa_columns)
        GameModes.UK -> applicationContext.resources.getInteger(R.integer.uk_columns)
        GameModes.AUSTRALIA -> applicationContext.resources.getInteger(R.integer.australia_columns)
    }
    val moneyValues = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getIntArray(R.array.usa_money)
        GameModes.UK -> applicationContext.resources.getIntArray(R.array.uk_money)
        GameModes.AUSTRALIA -> applicationContext.resources.getIntArray(R.array.australia_money)
    }
    val currency = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getString(R.string.usa_currency)
        GameModes.UK -> applicationContext.resources.getString(R.string.uk_currency)
        GameModes.AUSTRALIA -> applicationContext.resources.getString(R.string.australia_currency)
    }

    return GameData(moneyValues, rounds, currency, columns)
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreenComposable(
        rememberNavController(),
        GameScreen(
            GameModes.USA,
            isResumeGame = false
        )
    )
}