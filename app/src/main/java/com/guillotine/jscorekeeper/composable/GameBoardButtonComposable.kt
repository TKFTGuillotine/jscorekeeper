package com.guillotine.jscorekeeper.composable

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameBoardButton(
    text: String,
    fontFamily: FontFamily,
    fontSize: TextUnit = 50.sp,
    modifier: Modifier? = null,
    onClick: () -> Unit
) {
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
            fontFamily = fontFamily,
            fontSize = fontSize
        )
    }
}