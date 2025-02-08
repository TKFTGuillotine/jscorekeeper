package com.guillotine.jscorekeeper.composable.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.guillotine.jscorekeeper.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.guillotine.jscorekeeper.database.ScoreEntity
import com.guillotine.jscorekeeper.viewmodels.HistoryScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenComposable(viewModel: HistoryScreenViewModel) {
    val frontBackPadding = 8.dp
    val topBottomPadding = 24.dp
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    lateinit var pagedScores: LazyPagingItems<ScoreEntity>

    if (viewModel.isPagingSourceLoaded) {
        pagedScores = viewModel.getGamesList().collectAsLazyPagingItems()
    }

    Scaffold (topBar = {
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
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isPagingSourceLoaded) {
                items(
                    count = pagedScores.itemCount,
                    key = pagedScores.itemKey {it.timestamp}
                ) { gameIndex ->
                    val game = pagedScores[gameIndex]
                    if (game != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = topBottomPadding, bottom = topBottomPadding),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).padding(start = frontBackPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(dateFormat.format(game.timestamp))
                            }
                            Column(
                                modifier = Modifier.weight(1f).padding(end = frontBackPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(game.score.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}