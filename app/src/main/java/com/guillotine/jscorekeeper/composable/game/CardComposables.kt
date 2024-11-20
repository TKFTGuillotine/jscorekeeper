package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guillotine.jscorekeeper.ui.theme.ClueCardTheme
import com.guillotine.jscorekeeper.ui.theme.ScoreCardTheme

@Composable
fun ClueCardComposable(currency: String, value: Int) {
    ClueCardTheme {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                text = "${currency}${value}",
                style = MaterialTheme.typography.headlineSmall,
                // Again, this breaks spec, but in this case I think it looks best this way.
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ScoreCardComposable(currency: String, value: Int, modifier: Modifier = Modifier) {
    ScoreCardTheme {
        Card() {
            Text(
                modifier = modifier
                    .padding(top = 8.dp, bottom = 8.dp),
                text = if (value >= 0) {
                    // Provides a format string to apply the value to. The comma tells the function
                    // to use regionally appropriate separators with the value that comes next,
                    // which is an integer (d, much like in C). It's a modifier after the % you're
                    // used to from C to indicate the starting point.
                    //
                    // Format is a Java library and thus uses Java locale, but to get the current
                    // system locale into a Java locale, we have to grab the ISO 639 language code
                    // and pass it into the constructor.
                    String.format(java.util.Locale(Locale.current.language), "${currency}%,d", value)
                } else {
                    String.format(java.util.Locale(Locale.current.language), "-${currency}%,d", (value * -1))
                },
                style = MaterialTheme.typography.headlineSmall,
                // Again, this breaks spec, but in this case I think it looks best this way.
                textAlign = TextAlign.Center,
                color = if (value < 0) {
                    Color.Red
                } else {
                    Color.White
                }
            )
        }
    }
}