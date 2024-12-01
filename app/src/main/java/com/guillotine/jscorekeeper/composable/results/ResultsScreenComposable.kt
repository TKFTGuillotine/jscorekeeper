package com.guillotine.jscorekeeper.composable.results

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.datastore.dataStore
import androidx.navigation.NavHostController
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.ResultsScreen
import com.guillotine.jscorekeeper.data.SavedGameSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreenComposable(
    navController: NavHostController,
    route: ResultsScreen,
) {
    val score = route.score



    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(stringResource(R.string.results))
                }
            )
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                ResultsHorizontalComposable(score, innerPadding)
            }

            else -> {
                ResultsVerticalComposable(score, innerPadding)
            }
        }
    }
}