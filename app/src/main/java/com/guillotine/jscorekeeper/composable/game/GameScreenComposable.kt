package com.guillotine.jscorekeeper.composable.game

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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.data.ClueDialogState
import com.guillotine.jscorekeeper.viewmodels.GameScreenViewModel
import com.guillotine.jscorekeeper.data.GameScreenSnackbarVisuals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenComposable(navController: NavHostController, viewModel: GameScreenViewModel) {

    val noMoreDailyDoublesSnackbarVisuals =
        GameScreenSnackbarVisuals(stringResource(R.string.no_remaining_daily_doubles))

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
            Configuration.ORIENTATION_LANDSCAPE ->
                if (viewModel.isFinal) {

                } else {
                    GameBoardHorizontalComposable(
                        innerPadding = innerPadding,
                        moneyValues = viewModel.moneyValues,
                        currency = viewModel.currency,
                        score = viewModel.score,
                        onClueClick = { viewModel.showClueDialog(it) },
                        onNextRoundClick = { viewModel.showRoundDialog() },
                        isRemainingValue = viewModel::isValueRemaining,
                    )
                }

            else ->
                if (viewModel.isFinal) {
                    FinalVerticalComposable(
                        innerPadding = innerPadding,
                        score = viewModel.score,
                        currentSelectedOption = viewModel.currentSelectedClueDialogOption,
                        onOptionSelected = {viewModel.onClueDialogOptionSelected(it)},
                        wagerText = viewModel.wagerFieldText,
                        setWagerText = {viewModel.wagerFieldText = it},
                        isShowError = viewModel.isShowWagerFieldError,
                        currency = viewModel.currency,
                        submitFinalWager = viewModel::submitFinalWager,
                        navController = navController
                    )
                } else {
                    GameBoardVerticalComposable(
                        innerPadding = innerPadding,
                        moneyValues = viewModel.moneyValues,
                        currency = viewModel.currency,
                        score = viewModel.score,
                        onClueClick = { viewModel.showClueDialog(it) },
                        onNextRoundClick = { viewModel.showRoundDialog() },
                        isRemainingValue = viewModel::isValueRemaining,
                    )
                }


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
                listOfOptions = viewModel.getClueDialogOptions(),
                isWagerValid = viewModel::isWagerValid,
                clueDialogState = viewModel.clueDialogState,
                onNoMoreDailyDoubles = {
                    showSnackbar(
                        snackbarScope,
                        viewModel.snackbarHostState,
                        noMoreDailyDoublesSnackbarVisuals
                    )
                },
                onOptionSelected = {
                    viewModel.onClueDialogOptionSelected(it)
                },
                currentSelectedOption = viewModel.currentSelectedClueDialogOption,
                wagerText = viewModel.wagerFieldText,
                setWagerText = {viewModel.wagerFieldText = it},
                isShowError = viewModel.isShowWagerFieldError,
                setIsShowError = {viewModel.isShowWagerFieldError = it}
            )
        }
    }
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