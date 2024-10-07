package com.guillotine.jscorekeeper.composable

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guillotine.jscorekeeper.GameScreen
import com.guillotine.jscorekeeper.PastGamesListScreen
import com.guillotine.jscorekeeper.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenComposable(navController: NavHostController, applicationContext: Context?) {
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
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.40f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (applicationContext != null) {
                            navController.navigate(
                                GameScreen(
                                    rounds = applicationContext.resources.getInteger(
                                        R.integer.usa_rounds
                                    ),
                                    columns = applicationContext.resources.getInteger(
                                        R.integer.usa_columns
                                    ),
                                    moneyValues = applicationContext.resources.getIntArray(
                                        R.array.usa_money
                                    ),
                                    isResumeGame = false
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.play_us_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (applicationContext != null) {
                            navController.navigate(
                                GameScreen(
                                    rounds = applicationContext.resources.getInteger(
                                        R.integer.uk_rounds
                                    ),
                                    columns = applicationContext.resources.getInteger(
                                        R.integer.uk_columns
                                    ),
                                    moneyValues = applicationContext.resources.getIntArray(
                                        R.array.uk_money
                                    ),
                                    isResumeGame = false
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.play_uk_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (applicationContext != null) {
                            navController.navigate(
                                GameScreen(
                                    rounds = applicationContext.resources.getInteger(
                                        R.integer.australia_rounds
                                    ),
                                    columns = applicationContext.resources.getInteger(
                                        R.integer.australia_columns
                                    ),
                                    moneyValues = applicationContext.resources.getIntArray(
                                        R.array.australia_money
                                    ),
                                    isResumeGame = false
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.play_au_game))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(
                            PastGamesListScreen
                        )
                    }
                ) {
                    Text(stringResource(R.string.view_history))
                }
            }
        }
    }
}

@Preview
@Composable
fun MenuPreview() {
    MenuScreenComposable(rememberNavController(), null)
}