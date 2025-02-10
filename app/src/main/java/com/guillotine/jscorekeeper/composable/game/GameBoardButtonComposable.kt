package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.ui.theme.ClueButtonTheme
import com.guillotine.jscorekeeper.ui.theme.ClueButtonTypography

@Composable
fun GameBoardButton(
    text: String,
    modifier: Modifier? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    /* An inelegant solution but since I can't use BasicText due to styling issues with a disabled
       button, I'm going to shoehorn some state in here. If I wanted to do this super well I would
       find a way to auto resize as a group, but frankly if one or two values are smaller than the
       others, that's fine with me.
     */
    var resizeText by remember { mutableStateOf(false) }

    ClueButtonTheme {
        Button(
            shape = RoundedCornerShape(24.dp),
            modifier = modifier?.fillMaxWidth() ?: Modifier.fillMaxWidth(),
            onClick = onClick,
            enabled = enabled
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result: TextLayoutResult ->
                    if (result.hasVisualOverflow && !resizeText) {
                        resizeText = true
                    }
                },
                style = if (resizeText) {
                    ClueButtonTypography.labelSmall
                } else {
                    ClueButtonTypography.labelLarge
                }
            )

        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun GameBoardButtonPreview() {
    GameBoardButton(
        text = "$200",
        onClick = {}
    )
}

@Preview(apiLevel = 34)
@Composable
fun GameBoardButtonDisabledPreview() {
    GameBoardButton(
        text = "$200",
        onClick = {},
        enabled = false
    )
}