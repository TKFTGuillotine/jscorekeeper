package com.guillotine.jscorekeeper.composable.game

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guillotine.jscorekeeper.data.GameData
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.data.ClueDialogState
import com.guillotine.jscorekeeper.viewmodels.GameScreenViewModel
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.data.GameScreenSnackbarVisuals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenComposable(navController: NavHostController, gameScreenArgs: GameScreen) {
    val gameMode = gameScreenArgs.gameMode
    val isResumeGame = gameScreenArgs.isResumeGame
    val applicationContext = LocalContext.current.applicationContext

    val noMoreDailyDoublesSnackbarVisuals =
        GameScreenSnackbarVisuals(stringResource(R.string.no_remaining_daily_doubles))

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

    // Requires Context, so again, shouldn't be done in the ViewModel.
    val snackbarScope = rememberCoroutineScope()

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
                            stringArrayResource(R.array.round_names_indexed_by_multiplier)[viewModel.getMultiplier()]
                        } - ${stringResource(R.string.round)} ${viewModel.round + 1}"
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(viewModel.snackbarHostState)
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
                onNextRoundClick = { viewModel.showRoundDialog() },
                isRemainingValue = viewModel::isValueRemaining,
            )

            else -> GameBoardVerticalComposable(
                innerPadding = innerPadding,
                moneyValues = viewModel.moneyValues,
                currency = viewModel.currency,
                score = viewModel.score,
                onClueClick = { viewModel.showClueDialog(it) },
                onNextRoundClick = { viewModel.showRoundDialog() },
                isRemainingValue = viewModel::isValueRemaining,
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
        } else if (viewModel.clueDialogState != ClueDialogState.NONE) {
            ClueDialog(
                onDismissRequest = {
                    viewModel.onClueDialogDismiss()
                },
                value = viewModel.currentValue,
                currency = viewModel.currency,
                onCorrect = {
                    viewModel.onCorrectResponse(it)
                },
                onIncorrect = {
                    viewModel.onIncorrectResponse(it)
                },
                onPass = {
                    viewModel.onPass(it)
                },
                onDailyDouble = {
                    viewModel.onDailyDouble()
                },
                isRemainingDailyDouble = viewModel.isRemainingDailyDouble(),
                isWagerValid = viewModel::isWagerValid,
                clueDialogState = viewModel.clueDialogState,
                onNoMoreDailyDoubles = {
                    showSnackbar(
                        snackbarScope,
                        viewModel.snackbarHostState,
                        noMoreDailyDoublesSnackbarVisuals
                    )
                }
            )
        }
    }
}

private fun processGameData(applicationContext: Application, gameMode: GameModes): GameData {
    val rounds = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getIntArray(R.array.usa_round_multiplier)
        GameModes.UK -> applicationContext.resources.getIntArray(R.array.uk_round_multiplier)
        GameModes.AUSTRALIA -> applicationContext.resources.getIntArray(R.array.australia_round_multiplier)
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

private fun showSnackbar(
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    snackbarVisuals: SnackbarVisuals
) {
    snackbarScope.launch {
        snackbarHostState.showSnackbar(snackbarVisuals)
    }
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