package com.guillotine.jscorekeeper.composable.finalj

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.guillotine.jscorekeeper.FinalScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.viewmodels.FinalScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreenComposable(
    navController: NavHostController,
    viewModel: FinalScreenViewModel,
    route: FinalScreen
) {
    viewModel.round = route.round + 2
    viewModel.score = route.score
    viewModel.currency = route.currency

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
                            stringArrayResource(R.array.round_names_indexed_by_multiplier)[0]
                        } - ${stringResource(R.string.round)} ${viewModel.round}"
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                FinalHorizontalComposable(
                    innerPadding = innerPadding,
                    score = viewModel.score,
                    currentSelectedOption = viewModel.currentSelectedRadioButton,
                    onOptionSelected = { viewModel.onRadioButtonSelected(it) },
                    wagerText = viewModel.wagerText,
                    setWagerText = { viewModel.wagerText = it },
                    isShowError = viewModel.isShowError,
                    submitFinalWager = viewModel::submitFinalWager,
                    currency = viewModel.currency,
                    moneyValues = route.moneyValues,
                    multipliers = route.multipliers,
                    columns = route.columns,
                    navController = navController,
                    showError = viewModel::showError
                )
            }

            else -> {
                FinalVerticalComposable(
                    innerPadding = innerPadding,
                    score = viewModel.score,
                    currentSelectedOption = viewModel.currentSelectedRadioButton,
                    onOptionSelected = { viewModel.onRadioButtonSelected(it) },
                    wagerText = viewModel.wagerText,
                    setWagerText = { viewModel.wagerText = it },
                    isShowError = viewModel.isShowError,
                    submitFinalWager = viewModel::submitFinalWager,
                    currency = viewModel.currency,
                    moneyValues = route.moneyValues,
                    multipliers = route.multipliers,
                    columns = route.columns,
                    navController = navController,
                    showError = viewModel::showError
                )
            }

        }
    }
}