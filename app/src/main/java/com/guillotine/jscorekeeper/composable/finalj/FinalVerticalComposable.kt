package com.guillotine.jscorekeeper.composable.finalj

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
fun FinalVerticalComposable(
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
    Column(
        Modifier
            .padding(innerPadding)
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.entering_final_your_score_is)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreCardComposable(currency, score, true, Modifier.fillMaxWidth())
            }
        }
        HorizontalDivider()
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.final_wager_instructions),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WagerFieldComposable(
                    wagerText = wagerText,
                    setWagerText = setWagerText,
                    isShowError = isShowError,
                    currency = currency,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButtonList(
                currentSelectedOption = currentSelectedOption,
                onOptionSelected = onOptionSelected,
                listOfOptions = listOf(RadioButtonOptions.CORRECT, RadioButtonOptions.INCORRECT)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    // Placeholder, set this to whatever is consistent with the width of everything.
                    .padding(start = 0.dp, end = 0.dp)
                    .fillMaxWidth(),
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
                                score = score,
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