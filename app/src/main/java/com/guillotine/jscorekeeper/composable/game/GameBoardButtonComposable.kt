package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.ui.theme.ClueButtonTheme
import com.guillotine.jscorekeeper.ui.theme.ClueCardTheme

@Composable
fun GameBoardButton(
    text: String,
    modifier: Modifier? = null,
    onClick: () -> Unit
) {

    ClueButtonTheme {
        Button(
            shape = RoundedCornerShape(24.dp),
            modifier = modifier?.fillMaxWidth() ?: Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center
            )

        }
    }
}

@Preview
@Composable
fun GameBoardButtonPreview() {
    GameBoardButton(
        text = "$200",
        onClick = {}
    )
}