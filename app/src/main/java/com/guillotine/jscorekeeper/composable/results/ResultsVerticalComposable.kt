package com.guillotine.jscorekeeper.composable.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.composable.general.ScoreCardComposable
import com.guillotine.jscorekeeper.ui.theme.ScoreCardTheme
import com.guillotine.jscorekeeper.viewmodels.ResultsScreenViewModel

@Composable
fun ResultsVerticalComposable(
    viewModel: ResultsScreenViewModel,
    innerPadding: PaddingValues
) {
    val roundColumnPaddingValue = 10.dp
    val startEndPadding = 8.dp
    val graphColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .windowInsetsPadding(WindowInsets.displayCutout)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            ResultsGraphComposable(
                viewModel.scoreChanges,
                graphColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(startEndPadding)
            )
        }
        /* Could probably be a LazyColumn but there's not *that* much data and I don't wanna deal
           with it.
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
                .verticalScroll(rememberScrollState())
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

@Preview
@Composable
fun NestedLayoutTestComposable() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Final Score:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        ScoreCardTheme {
            ScoreCardComposable("$", 15800, true, modifier = Modifier.fillMaxWidth())
        }
        // Each round column gets a row for the round header.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Round 1")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$200")
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$400")
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$600")
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$800")
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$1000")
                HorizontalDivider(Modifier.fillMaxWidth())
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_clear_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_block_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_star_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_star_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())

            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                HorizontalDivider(Modifier.fillMaxWidth())
            }
        }

        // Daily Doubles get a column.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.daily_doubles))
            }
            HorizontalDivider(Modifier.fillMaxWidth())
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Round 1")
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$2500")
                }
                Row(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_check_24),
                        contentDescription = stringResource(R.string.correct_icon_description)
                    )
                }
            }
            HorizontalDivider(Modifier.fillMaxWidth())
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringArrayResource(R.array.round_names_indexed_by_multiplier)[0])
            }
            HorizontalDivider(Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("$5000")
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
            }
            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }
}