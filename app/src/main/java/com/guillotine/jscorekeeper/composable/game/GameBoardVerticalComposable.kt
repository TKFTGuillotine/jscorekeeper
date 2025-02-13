package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.composable.general.ScoreCardComposable

@Composable
fun GameBoardVerticalComposable(
    innerPadding: PaddingValues,
    moneyValues: IntArray,
    currency: String,
    score: Int,
    onNextRoundClick: () -> Unit,
    onClueClick: (Int) -> Unit,
    isRemainingValue: (Int) -> Boolean
) {
    // For spacing buttons
    val verticalSpacing = 8.dp

    Row(
        modifier = Modifier
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .windowInsetsPadding(WindowInsets.displayCutout)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.size(verticalSpacing))
            // Often you wouldn't want to do this, but since the number of items is small and I want
            // them all available, this seems fine.
            moneyValues.forEach {
                GameBoardButton(
                    text = "${currency}$it",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onClueClick(it)
                    },
                    enabled = isRemainingValue(it)
                )
                Spacer(Modifier.size(verticalSpacing))
            }

        }
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScoreCardComposable(currency, score, false, Modifier.fillMaxWidth())
            Button(
                onClick = { onNextRoundClick() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(R.string.next_round))
            }
        }
    }
}

@Preview
@Composable
fun GameBoardVerticalComposablePreview () {
    GameBoardVerticalComposable(
        innerPadding = PaddingValues(0.dp),
        moneyValues = intArrayOf(200, 400, 600, 800, 1000),
        currency = "$",
        score = 1600,
        onNextRoundClick = {},
        onClueClick = {},
        isRemainingValue = {true}
    )
}