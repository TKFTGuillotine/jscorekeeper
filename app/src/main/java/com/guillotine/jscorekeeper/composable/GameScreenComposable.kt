package com.guillotine.jscorekeeper.composable

import android.app.Application
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.guillotine.jscorekeeper.GameData
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.viewmodels.GameScreenViewModel
import com.guillotine.jscorekeeper.GameModes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenComposable(navController: NavHostController, gameScreenArgs: GameScreen) {
    val gameMode = gameScreenArgs.gameMode
    val isResumeGame = gameScreenArgs.isResumeGame
    val applicationContext = LocalContext.current.applicationContext

    val viewModel = viewModel {
        // It feels wrong to me handling this resource data here, but Google themselves recommend
        // moving any Context access up to the UI layer. Nonetheless, I have it pulled out into a
        // different function and not *stored* anywhere here such that I don't accidentally refer to
        // the wrong data later.
        GameScreenViewModel(processGameData(applicationContext as Application, gameMode), isResumeGame)
    }

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
                Text(
                    "${
                        if (viewModel.isDoubleJ()) {
                            stringResource(R.string.double_j)
                        } else {
                            stringResource(R.string.j)
                        }
                    } - ${stringResource(R.string.round)} ${viewModel.round + 1}"
                )
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
                viewModel.moneyValues.forEach {
                    GameBoardButton(
                        text = "${viewModel.currency}$it",
                        fontFamily = bebasNeueFamily,
                        modifier = Modifier.weight(1f),
                        onClick = {}
                    )
                    Spacer(Modifier.size(VERTICAL_SPACING))
                }

            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${stringResource(R.string.score)}: ${viewModel.score}")
                Button(
                    onClick = {
                        viewModel.showRoundDialog()
                    }
                ) {
                    Text(text = stringResource(R.string.next_round))
                }
            }
        }

        if (viewModel.isShowRoundDialog) {
            NextRoundDialog(
                onDismissRequest = {
                    viewModel.onRoundDialogDismiss()
                },
                onConfirmation = {
                    viewModel.nextRound()
                }
            )
        }

    }
}

fun processGameData(applicationContext: Application, gameMode: GameModes) : GameData {
    val rounds = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getInteger(R.integer.usa_rounds)
        GameModes.UK -> applicationContext.resources.getInteger(R.integer.uk_rounds)
        GameModes.AUSTRALIA -> applicationContext.resources.getInteger(R.integer.australia_rounds)
    }
    val columns = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getInteger(R.integer.usa_columns)
        GameModes.UK -> applicationContext.resources.getInteger(R.integer.uk_columns)
        GameModes.AUSTRALIA -> applicationContext.resources.getInteger(R.integer.australia_columns)
    }
    val moneyValues = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getIntArray(R.array.usa_money)
        GameModes.UK -> applicationContext.resources.getIntArray(R.array.uk_money)
        GameModes.AUSTRALIA -> applicationContext.resources.getIntArray(R.array.australia_money)
    }
    val currency = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getString(R.string.usa_currency)
        GameModes.UK -> applicationContext.resources.getString(R.string.uk_currency)
        GameModes.AUSTRALIA -> applicationContext.resources.getString(R.string.australia_currency)
    }

    return GameData(moneyValues, rounds, currency, columns)
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreenComposable(
        rememberNavController(),
        GameScreen(
            GameModes.USA,
            isResumeGame = false
        )
    )
}