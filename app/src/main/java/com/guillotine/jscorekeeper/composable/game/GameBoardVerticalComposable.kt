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
    fontFamily: FontFamily,
    onNextRoundClick: () -> Unit,
    onClueClick: (Int) -> Unit
) {
    // For spacing buttons
    val VERTICAL_SPACING = 8.dp

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
            Spacer(Modifier.size(VERTICAL_SPACING))
            // Often you wouldn't want to do this, but since the number of items is small and I want
            // them all available, this seems fine.
            moneyValues.forEach {
                GameBoardButton(
                    text = "${currency}$it",
                    fontFamily = fontFamily,
                    modifier = Modifier.weight(1f),
                    onClick = {onClueClick(it)}
                )
                Spacer(Modifier.size(VERTICAL_SPACING))
            }

        }
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${stringResource(R.string.score)}: ${currency}${score}")
            Button(
                onClick = {onNextRoundClick()}
            ) {
                Text(text = stringResource(R.string.next_round))
            }
        }
    }

}