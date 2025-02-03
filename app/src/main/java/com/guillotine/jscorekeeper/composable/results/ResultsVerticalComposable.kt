package com.guillotine.jscorekeeper.composable.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.database.ClueType
import com.guillotine.jscorekeeper.viewmodels.ResultsScreenViewModel

@Composable
fun ResultsVerticalComposable(
    viewModel: ResultsScreenViewModel,
    innerPadding: PaddingValues
) {
    val roundColumnPaddingValue = 10.dp
    val startEndPadding = 8.dp

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) { Text("Graph goes here.") }
        /* Could probably be a LazyColumn but there's not *that* much data and I don't wanna deal
           with it.
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
                .verticalScroll(rememberScrollState())
        ) {
            // Each round gets its own column.
            for (roundNumber in viewModel.cluesByRound.indices) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = roundColumnPaddingValue),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Each round column gets a row for the round header.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(stringResource(R.string.round) + " " + (roundNumber + 1))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Each round gets one column for each value.
                        for (j in 0..(viewModel.cluesByRound[roundNumber].size - 1)) {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Each value column gets a value header.
                                Text(viewModel.getCurrency() + viewModel.getValues()[j] * viewModel.getMultipliers()[roundNumber])
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Each round gets one column for each value.
                        for (j in 0..(viewModel.cluesByRound[roundNumber].size - 1)) {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Each value column gets the icons for each result.
                                for (clue in viewModel.cluesByRound[roundNumber][j]) {
                                    when (clue) {
                                        ClueType.CORRECT -> {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_check_24),
                                                contentDescription = stringResource(R.string.correct_icon_description)
                                            )
                                        }

                                        ClueType.INCORRECT -> {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_clear_24),
                                                contentDescription = stringResource(R.string.incorrect_icon_description)
                                            )
                                        }

                                        ClueType.PASS -> {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_block_24),
                                                contentDescription = stringResource(R.string.correct_icon_description)
                                            )
                                        }

                                        ClueType.DAILY_DOUBLE -> {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_star_24),
                                                contentDescription = stringResource(R.string.daily_double_icon_description)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Daily Doubles get a column.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = roundColumnPaddingValue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.daily_doubles))
                }
                for (dailyDouble in viewModel.dailyDoubles) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = startEndPadding)
                                .weight(1f),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.round) + " " + (dailyDouble.round + 1))
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(viewModel.getCurrency() + dailyDouble.wager.toString())
                        }
                        Row(
                            modifier = Modifier
                                .padding(end = startEndPadding)
                                .weight(1f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when (dailyDouble.wasCorrect) {
                                true -> {
                                    Image(
                                        painter = painterResource(R.drawable.baseline_check_24),
                                        contentDescription = stringResource(R.string.correct_icon_description)
                                    )
                                }

                                false -> {
                                    Image(
                                        painter = painterResource(R.drawable.baseline_clear_24),
                                        contentDescription = stringResource(R.string.incorrect_icon_description)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Final gets a column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = roundColumnPaddingValue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringArrayResource(R.array.round_names_indexed_by_multiplier)[0])
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = startEndPadding)
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(viewModel.getCurrency() + viewModel.finalEntity?.wager)

                        when (viewModel.finalEntity?.correct) {
                            true -> {
                                Image(
                                    painter = painterResource(R.drawable.baseline_check_24),
                                    contentDescription = stringResource(R.string.correct_icon_description)
                                )
                            }

                            false -> {
                                Image(
                                    painter = painterResource(R.drawable.baseline_clear_24),
                                    contentDescription = stringResource(R.string.incorrect_icon_description)
                                )
                            }

                            null -> {}
                        }
                    }
                }
            }

        }
        /*items(1) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.daily_doubles))
                for (dailyDouble in viewModel.dailyDoubles) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.round) + " " + dailyDouble.round)
                        Text(viewModel.getCurrency() + dailyDouble.wager.toString())
                        when (dailyDouble.wasCorrect) {
                            true -> {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_check_24),
                                    contentDescription = stringResource(R.string.correct_icon_description)
                                )
                            }
                            false -> {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_clear_24),
                                    contentDescription = stringResource(R.string.incorrect_icon_description)
                                )
                            }
                        }
                    }
                }
            }
        }*/
        /*items(1) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringArrayResource(R.array.round_names_indexed_by_multiplier)[0])
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(viewModel.getCurrency() + viewModel.finalEntity?.wager.toString())
                    when (viewModel.finalEntity?.correct) {
                        true -> {
                            Icon(
                                painter = painterResource(R.drawable.baseline_check_24),
                                contentDescription = stringResource(R.string.correct_icon_description)
                            )
                        }
                        false -> {
                            Icon(
                                painter = painterResource(R.drawable.baseline_clear_24),
                                contentDescription = stringResource(R.string.incorrect_icon_description)
                            )
                        }
                        null -> {}
                    }
                }
            }
        }*/
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
        // Each round column gets a row for the round header.
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$400")
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$600")
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$800")
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$1000")
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
                Image(
                    painter = painterResource(R.drawable.baseline_clear_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_block_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_star_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_star_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )

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
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
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
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
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
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
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
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.correct_icon_description)
                )
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.daily_doubles))
            }
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringArrayResource(R.array.round_names_indexed_by_multiplier)[0])
            }
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


        }
    }
}