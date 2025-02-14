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
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.database.ScoreEntity
import com.guillotine.jscorekeeper.database.StatisticsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HistoryScreenViewModel(private val statisticsDatabase: StatisticsDatabase) : ViewModel() {
    private lateinit var gamesPagingSource: PagingSource<Int, ScoreEntity>
    var isPagingSourceLoaded by mutableStateOf(false)
        private set

    // Number items per page.
    private val pageSize = 20

    init {
        viewModelScope.launch {
            gamesPagingSource = statisticsDatabase.statisticsDao().getScoresPagingSource()
                .also { isPagingSourceLoaded = true }
        }
    }

    fun getGamesPageData(): Flow<PagingData<ScoreEntity>> = Pager(
        PagingConfig(
            pageSize = pageSize
        )
    ) {
        gamesPagingSource
    }.flow.map{ pagingData: PagingData<ScoreEntity> ->
        pagingData.map { scoreEntity ->
            ScoreEntity(scoreEntity.timestamp, scoreEntity.score)
        }
    }

    fun getGamesList(): Flow<PagingData<ScoreEntity>> {
        return getGamesPageData().cachedIn(viewModelScope)
    }

    suspend fun getGameMode(timestamp: Long): GameModes {
        return statisticsDatabase.statisticsDao().getGame(timestamp)!!.gameMode
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
