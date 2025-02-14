package com.guillotine.jscorekeeper.composable.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.composable.general.ScoreCardComposable
import com.guillotine.jscorekeeper.ui.theme.ScoreCardTheme
import com.guillotine.jscorekeeper.viewmodels.ResultsScreenViewModel

@Composable
fun ResultsHorizontalComposable(
    viewModel: ResultsScreenViewModel,
    innerPadding: PaddingValues
) {

    val roundColumnPaddingValue = 10.dp
    val startEndPadding = 8.dp
    val graphColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .windowInsetsPadding(WindowInsets.displayCutout)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(roundColumnPaddingValue),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.final_score),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = roundColumnPaddingValue)
                            .fillMaxWidth()
                    )
                    ScoreCardTheme {
                        ScoreCardComposable(
                            viewModel.getCurrency(),
                            viewModel.getScore(),
                            true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(roundColumnPaddingValue),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ResultsGraphComposable(
                    viewModel.scoreChanges,
                    graphColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(startEndPadding)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ResultsStatsColumnComposable(
                roundColumnPaddingValue,
                startEndPadding,
                viewModel.cluesByRound,
                viewModel.getCurrency(),
                viewModel.getValues(),
                viewModel.getMultipliers(),
                viewModel.dailyDoubles,
                viewModel.finalEntity
            )
        }
    }
}