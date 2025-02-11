package com.guillotine.jscorekeeper.composable.finalj

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.guillotine.jscorekeeper.MenuScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.ResultsScreen
import com.guillotine.jscorekeeper.composable.general.ScoreCardComposable
import com.guillotine.jscorekeeper.composable.general.WagerFieldComposable
import com.guillotine.jscorekeeper.data.RadioButtonOptions
import com.guillotine.jscorekeeper.composable.general.RadioButtonList
import com.guillotine.jscorekeeper.data.GameData

@Composable
fun FinalHorizontalComposable(
    innerPadding: PaddingValues,
    score: Int,
    currentSelectedOption: RadioButtonOptions,
    onOptionSelected: (RadioButtonOptions) -> Unit,
    wagerText: String,
    setWagerText: (String) -> Unit,
    isShowError: Boolean,
    currency: String,
    submitFinalWager: (Int, Int, Boolean) -> Long?,
    moneyValues: IntArray,
    multipliers: IntArray,
    columns: Int,
    navController: NavHostController
) {
    // The whole scaffold thing doesn't seem to account for the display cutout, so I'll handle it
    // myself here. It works fine in portrait because of the cutout being a part of the status bar,
    // so that's nice at least.
    val displayCutoutPadding = WindowInsets.displayCutout.asPaddingValues()
    Column(
        Modifier
            .padding(innerPadding)
            .padding(displayCutoutPadding)
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.entering_final_your_score_is)
                )
                Row(Modifier.padding(top = 16.dp)) {
                    ScoreCardComposable(currency, score, true, Modifier.fillMaxWidth())
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).padding(start = 16.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.final_wager_instructions),
                    textAlign = TextAlign.Center
                )
                WagerFieldComposable(
                    wagerText = wagerText,
                    setWagerText = setWagerText,
                    isShowError = isShowError,
                    currency = currency,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).padding(start = 16.dp, end = 16.dp)
            ) {
                RadioButtonList(
                    currentSelectedOption = currentSelectedOption,
                    onOptionSelected = onOptionSelected,
                    listOfOptions = listOf(RadioButtonOptions.CORRECT, RadioButtonOptions.INCORRECT),
                    // Since parent is scrollable when overflow, this must not be to compile.
                    scrollable = false
                )
                Button(
                    modifier = Modifier
                        // Placeholder, set this to whatever is consistent with the width of everything.
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {
                        val timestamp = submitFinalWager(
                            wagerText.toInt(),
                            score,
                            currentSelectedOption == RadioButtonOptions.CORRECT
                        )
                        if (timestamp != null) {
                            navController.navigate(
                                route = ResultsScreen(
                                    timestamp = timestamp,
                                    score = if (currentSelectedOption == RadioButtonOptions.CORRECT) {score + wagerText.toInt()} else {score - wagerText.toInt()},
                                    moneyValues = moneyValues,
                                    multipliers = multipliers,
                                    currency = currency,
                                    columns = columns,
                                    deleteCurrentSavedGame = true
                                )
                            ) {
                                popUpTo(MenuScreen) {
                                    inclusive = false
                                }
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
        }
    }
}