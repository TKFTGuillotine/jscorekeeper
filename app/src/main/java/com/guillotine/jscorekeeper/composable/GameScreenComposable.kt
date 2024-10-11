package com.guillotine.jscorekeeper.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.viewmodels.GameScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenComposable(navController: NavHostController, gameScreenArgs: GameScreen) {
    val rounds = gameScreenArgs.rounds
    val columns = gameScreenArgs.columns
    val currency = gameScreenArgs.currency
    val moneyValues = gameScreenArgs.moneyValues
    val isResumeGame = gameScreenArgs.isResumeGame

    val viewModel = viewModel<GameScreenViewModel>(
        factory = object: ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return GameScreenViewModel(rounds) as T
            }
        }
    )

    // For spacing buttons
    val VERTICAL_SPACING = 8.dp

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
                Text("${
                    if (rounds - 2 == viewModel.round) {
                        stringResource(R.string.double_j)
                    } else {
                        stringResource(R.string.j)
                    }
                } - ${stringResource(R.string.round)} ${viewModel.round + 1}")
            }
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                    .width(IntrinsicSize.Max)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.size(VERTICAL_SPACING))
                // Often you wouldn't want to do this, but since the number of items is small and I want
                // them all available, this seems fine.
                moneyValues.forEach {
                    GameBoardButton(
                        text = "$currency$it",
                        fontFamily = bebasNeueFamily,
                        modifier = Modifier.weight(1f),
                        onClick = {}
                    )
                    Spacer(Modifier.size(VERTICAL_SPACING))
                }

            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreenComposable(
        rememberNavController(),
        GameScreen(
            rounds = 3,
            columns = 6,
            currency = "$",
            moneyValues = intArrayOf(200, 400, 600, 800, 1000),
            isResumeGame = false
        )
    )
}