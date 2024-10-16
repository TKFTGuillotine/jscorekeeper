package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.R

@Composable
fun GameBoardVerticalComposable(
    innerPadding: PaddingValues,
    moneyValues: IntArray,
    currency: String,
    score: Int,
    onNextRoundClick: () -> Unit,
    onClueClick: (Int) -> Unit
) {
    // For spacing buttons
    val verticalSpacing = 8.dp

    Row(
        modifier = Modifier
            .padding(innerPadding)
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
                    }
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
            // I'd rather the negative be displayed before the currency symbol, as on the show.
            if (score >= 0) {
                Text("${stringResource(R.string.score)}: ${currency}${score}")
            } else {
                Text("${stringResource(R.string.score)}: -${currency}${Math.abs(score)}")
            }
            Button(
                onClick = {onNextRoundClick()}
            ) {
                Text(text = stringResource(R.string.next_round))
            }
        }
    }

}