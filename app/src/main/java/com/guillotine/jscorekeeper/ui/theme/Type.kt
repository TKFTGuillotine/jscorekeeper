package com.guillotine.jscorekeeper.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.guillotine.jscorekeeper.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val bebasNeueFamily = FontFamily(Font(R.font.bebas_neue))
val poppinsFamily = FontFamily(Font(R.font.poppins_bold))

val ClueButtonTypography = Typography(
    labelLarge = TextStyle(
        fontFamily = bebasNeueFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 50.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = bebasNeueFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 50.sp
    )
)

val ScoreCardTypography = Typography(
    labelLarge = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 50.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = poppinsFamily,
        fontSize = 50.sp
    )
)