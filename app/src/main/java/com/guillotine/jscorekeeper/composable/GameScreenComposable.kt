package com.guillotine.jscorekeeper.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenComposable(navController: NavHostController, gameScreenArgs: GameScreen) {
    val rounds = gameScreenArgs.rounds
    val columns = gameScreenArgs.columns
    val moneyValues = gameScreenArgs.moneyValues
    val isResumeGame = gameScreenArgs.isResumeGame

    // Font provider, from which we can pull the gameboard font approximation.
    val fontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    // Looks close enough to Swiss 911 without requiring I license the thing for money.
    val bebasNeue = GoogleFont(name = "Bebas Neue")

    // Can be passed into a Text Composable, finally.
    val bebasNeueFamily = FontFamily(
        Font(googleFont = bebasNeue, fontProvider = fontProvider)
    )

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text(stringResource(R.string.app_name))
            }
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("$200", fontFamily = bebasNeueFamily, fontSize = 50.sp)
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreenComposable(
        rememberNavController(),
        GameScreen(3, 6, intArrayOf(200, 400, 600, 800, 1000), false)
    )
}