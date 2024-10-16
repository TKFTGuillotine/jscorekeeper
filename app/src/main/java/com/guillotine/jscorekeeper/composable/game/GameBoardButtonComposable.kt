package com.guillotine.jscorekeeper.composable.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guillotine.jscorekeeper.R

@Composable
fun GameBoardButton(
    text: String,
    fontSize: TextUnit = 50.sp,
    modifier: Modifier? = null,
    onClick: () -> Unit
) {
    // This turns out to create much less headache on many fronts than async fonts.
    val bebasNeueFamily = FontFamily(Font(R.font.bebas_neue))

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00178E),
            contentColor = Color(0xFFFBB43E)
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier?.fillMaxWidth() ?: Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(
            text = text,
            fontFamily = bebasNeueFamily,
            fontSize = fontSize,
            textAlign = TextAlign.Center
        )

    }
}
