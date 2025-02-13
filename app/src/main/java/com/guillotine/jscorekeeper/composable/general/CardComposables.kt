package com.guillotine.jscorekeeper.composable.general

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
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
fun ScoreCardComposable(
    currency: String,
    value: Int,
    isFinal: Boolean,
    modifier: Modifier = Modifier
) {
    /* An inelegant solution but since I can't use BasicText due to styling issues with a disabled
       button, I'm going to shoehorn some state in here. If I wanted to do this super well I would
       find a way to auto resize as a group, but frankly if one or two values are smaller than the
       others, that's fine with me.
     */
    var resizeText by remember { mutableStateOf(false) }

    Card(
        shape = ShapeDefaults.ExtraSmall,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF996445)
        ),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        ScoreCardTheme {
            Card(
                shape = ShapeDefaults.ExtraSmall,
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    modifier = modifier
                        .padding(top = 4.dp, bottom = 4.dp),
                    text = if (value >= 0) {
                        // Provides a format string to apply the value to. The comma tells the function
                        // to use regionally appropriate separators with the value that comes next,
                        // which is an integer (d, much like in C). It's a modifier after the % you're
                        // used to from C to indicate the starting point.
                        //
                        // Format is a Java library and thus uses Java locale, but to get the current
                        // system locale into a Java locale, we have to grab the ISO 639 language code
                        // and pass it into the constructor.
                        String.format(
                            java.util.Locale(Locale.current.language),
                            "${currency}%,d",
                            value
                        )
                    } else {
                        String.format(
                            java.util.Locale(Locale.current.language),
                            "-${currency}%,d",
                            (value * -1)
                        )
                    },
                    maxLines = 1,
                    onTextLayout = { result: TextLayoutResult ->
                        if (result.hasVisualOverflow && !resizeText) {
                            resizeText = true
                        }
                    },
                    style = if (isFinal && !resizeText) {
                        MaterialTheme.typography.labelLarge
                    } else if (!isFinal && !resizeText){
                        MaterialTheme.typography.bodyLarge
                    } else if (isFinal && resizeText) {
                        MaterialTheme.typography.labelSmall
                    } else {
                        MaterialTheme.typography.bodySmall
                    },
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
}