package com.guillotine.jscorekeeper.composable.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.viewmodels.ResultsScreenViewModel

@Composable
fun ResultsHorizontalComposable(
    viewModel: ResultsScreenViewModel,
    innerPadding: PaddingValues
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("placeholder")
    }
}