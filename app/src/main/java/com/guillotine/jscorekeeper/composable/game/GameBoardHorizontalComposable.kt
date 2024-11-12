package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.R

@Composable
fun GameBoardHorizontalComposable(
    innerPadding: PaddingValues,
    moneyValues: IntArray,
    currency: String,
    score: Int,
    onNextRoundClick: () -> Unit,
    onClueClick: (Int) -> Unit,
    isRemainingValue: (Int) -> Boolean
) {
    // For spacing buttons
    val horizontalSpacing = 8.dp
    // The whole scaffold thing doesn't seem to account for the display cutout, so I'll handle it
    // myself here. It works fine in portrait because of the cutout being a part of the status bar,
    // so that's nice at least.
    val displayCutoutPadding = WindowInsets.displayCutout.asPaddingValues()

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(displayCutoutPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // I'd rather the negative be displayed before the currency symbol, as on the show.
            if (score >= 0) {
                Text("${stringResource(R.string.score)}: ${currency}${score}")
            } else {
                Text("${stringResource(R.string.score)}: -${currency}${Math.abs(score)}")
            }
            Button(
                onClick = { onNextRoundClick() }
            ) {
                Text(text = stringResource(R.string.next_round))
            }
        }

        Row(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.size(horizontalSpacing))
            // Often you wouldn't want to do this, but since the number of items is small and I want
            // them all available, this seems fine.
            moneyValues.forEach {
                GameBoardButton(
                    text = "${currency}$it",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    enabled = isRemainingValue(it),
                    onClick = {
                        onClueClick(it)
                    }

                )
                Spacer(Modifier.size(horizontalSpacing))
            }

        }
    }

}