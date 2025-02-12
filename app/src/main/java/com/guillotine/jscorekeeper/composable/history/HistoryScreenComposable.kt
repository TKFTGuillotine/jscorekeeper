package com.guillotine.jscorekeeper.composable.history

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.guillotine.jscorekeeper.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.guillotine.jscorekeeper.ResultsScreen
import com.guillotine.jscorekeeper.data.processGameData
import com.guillotine.jscorekeeper.database.ScoreEntity
import com.guillotine.jscorekeeper.viewmodels.HistoryScreenViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenComposable(
    viewModel: HistoryScreenViewModel,
    navigationController: NavHostController
) {
    val frontBackPadding = 8.dp
    val rowHeight = 64.dp
    val context = LocalContext.current
    /* Use the language from Android locale to convert into a Java locale, since that will recompose
       if it changes at runtime. Just like in the cards. Using YYYY-MM-DD format because in theory
       it should be understandable no matter what country the user is from.
     */
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", java.util.Locale(Locale.current.language))
    lateinit var pagedScores: LazyPagingItems<ScoreEntity>
    val coroutineScope = rememberCoroutineScope()

    if (viewModel.isPagingSourceLoaded) {
        pagedScores = viewModel.getGamesList().collectAsLazyPagingItems()
    }

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
        LazyColumn(
            modifier =
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Modifier
                    .padding(innerPadding)
                    .padding(WindowInsets.displayCutout.asPaddingValues())
                    .fillMaxSize()
            } else {
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isPagingSourceLoaded && pagedScores.itemCount != 0) {
                item { HorizontalDivider() }
                items(
                    count = pagedScores.itemCount,
                    key = pagedScores.itemKey { it.timestamp }
                ) { gameIndex ->
                    val game = pagedScores[gameIndex]
                    if (game != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(rowHeight)
                                .clickable {
                                    coroutineScope.launch {
                                        val gameData = processGameData(
                                            applicationContext = context,
                                            gameMode = viewModel.getGameMode(game.timestamp)
                                        )

                                        navigationController.navigate(
                                            ResultsScreen(
                                                timestamp = game.timestamp,
                                                score = game.score,
                                                moneyValues = gameData.moneyValues,
                                                multipliers = gameData.multipliers,
                                                columns = gameData.columns,
                                                currency = gameData.currency,
                                                deleteCurrentSavedGame = false
                                            )
                                        )
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = frontBackPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(dateFormat.format(game.timestamp))
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = frontBackPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(game.score.toString())
                            }
                        }
                        HorizontalDivider()
                    }
                }
            } else {
                item {
                    Text(
                        text = stringResource(R.string.past_games_here),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(frontBackPadding)
                    )
                }
            }
        }
    }
}