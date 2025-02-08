package com.guillotine.jscorekeeper.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guillotine.jscorekeeper.database.ScoreEntity
import com.guillotine.jscorekeeper.database.StatisticsDatabase
import kotlinx.coroutines.launch

class HistoryScreenViewModel(private val statisticsDatabase: StatisticsDatabase) : ViewModel() {
    var gamesList by mutableStateOf(listOf<ScoreEntity>())
        private set

    init {
        viewModelScope.launch {
            gamesList = statisticsDatabase.statisticsDao().getScoresList()
        }
    }

    companion object {
        val STATISTICS_DATABASE_KEY = object : CreationExtras.Key<StatisticsDatabase> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val statisticsDatabase = (this[STATISTICS_DATABASE_KEY] as StatisticsDatabase)
                HistoryScreenViewModel(statisticsDatabase)
            }
        }
    }
}
