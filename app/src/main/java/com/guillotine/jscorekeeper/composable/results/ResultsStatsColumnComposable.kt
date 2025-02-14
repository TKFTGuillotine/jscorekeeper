package com.guillotine.jscorekeeper.composable.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.database.ClueType
import com.guillotine.jscorekeeper.database.DailyDoubleEntity
import com.guillotine.jscorekeeper.database.FinalEntity

@Composable
fun ResultsStatsColumnComposable(
    roundColumnPaddingValue: Dp,
    startEndPadding: Dp,
    cluesByRound: List<List<List<ClueType>>>,
    currency: String,
    values: IntArray,
    multipliers: IntArray,
    dailyDoubles: List<DailyDoubleEntity>,
    finalEntity: FinalEntity?
    ) {
    // Each round gets its own column.
    for (roundNumber in cluesByRound.indices) {
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
                    .fillMaxWidth().padding(bottom = roundColumnPaddingValue),
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
                for (j in 0..(cluesByRound[roundNumber].size - 1)) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Each value column gets a value header.
                        HorizontalDivider(Modifier.fillMaxWidth())
                        Text(text = currency + values[j] * multipliers[roundNumber], maxLines = 1, overflow = TextOverflow.Ellipsis)
                        HorizontalDivider(Modifier.fillMaxWidth())
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Each round gets one column for each value.
                for (j in 0..(cluesByRound[roundNumber].size - 1)) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Each value column gets the icons for each result.
                        for (clue in cluesByRound[roundNumber][j]) {
                            when (clue) {
                                ClueType.CORRECT -> {
                                    Image(
                                        painter = painterResource(R.drawable.baseline_check_24),
                                        contentDescription = stringResource(R.string.correct_icon_description)
                                    )
                                    HorizontalDivider(Modifier.fillMaxWidth())
                                }

                                ClueType.INCORRECT -> {
                                    Image(
                                        painter = painterResource(R.drawable.baseline_clear_24),
                                        contentDescription = stringResource(R.string.incorrect_icon_description)
                                    )
                                    HorizontalDivider(Modifier.fillMaxWidth())
                                }

                                ClueType.PASS -> {
                                    Image(
                                        painter = painterResource(R.drawable.baseline_block_24),
                                        contentDescription = stringResource(R.string.correct_icon_description)
                                    )
                                    HorizontalDivider(Modifier.fillMaxWidth())
                                }

                                ClueType.DAILY_DOUBLE -> {
                                    Image(
                                        painter = painterResource(R.drawable.baseline_star_24),
                                        contentDescription = stringResource(R.string.daily_double_icon_description)
                                    )
                                    HorizontalDivider(Modifier.fillMaxWidth())
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
                .fillMaxWidth().padding(bottom = roundColumnPaddingValue),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(R.string.daily_doubles))
        }
        HorizontalDivider(Modifier.fillMaxWidth())
        for (dailyDouble in dailyDoubles) {
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
                    Text(currency + dailyDouble.wager.toString())
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
            HorizontalDivider(Modifier.fillMaxWidth())
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
                .fillMaxWidth().padding(bottom = roundColumnPaddingValue),
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
            Row(
                modifier = Modifier
                    .padding(start = startEndPadding)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(currency + finalEntity?.wager)

                when (finalEntity?.correct) {
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
        HorizontalDivider(Modifier.fillMaxWidth())
    }
}