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
import com.guillotine.jscorekeeper.database.GameEntity
import com.guillotine.jscorekeeper.database.StatisticsDatabase
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.data.SavedGame
import kotlinx.coroutines.launch

class MenuScreenViewModel(
    private val statisticsDatabase: StatisticsDatabase
) : ViewModel() {
    var loadedTimestamp = false
        private set
    var attemptedLoadTimestamp by mutableStateOf(false)
    var timestamp by mutableStateOf(0L)
        private set

    init {
        println("MenuScreenViewModel init")
    }

    fun isSavedGame(savedGame: SavedGame?): Boolean {
        // Arbitrary, but it would be impossible to have a board with no columns, so this being a
        // default value in the serializer, it's an easy way to tell if there's a game or not.
        return savedGame?.gameData?.columns != 0
    }

    fun createGame(mode: GameModes) {
        timestamp = System.currentTimeMillis()
        viewModelScope.launch {
            statisticsDatabase.statisticsDao().createGame(
                GameEntity(
                    gameMode = mode,
                    timestamp = timestamp,
                    visible = false
                )
            )
        }
    }

    fun deleteSavedGame() {
        viewModelScope.launch {
            // Deletes from Room
            statisticsDatabase.statisticsDao().deleteSavedGame()
        }
    }

        suspend fun loadTimestamp() {
            val savedGameEntity = statisticsDatabase.statisticsDao().getSavedGame()
            if (savedGameEntity != null && savedGameEntity.timestamp != 0L) {
                timestamp = savedGameEntity.timestamp
                loadedTimestamp = true
            }
            attemptedLoadTimestamp = true
        }


        companion object {
            val STATISTICS_DATABASE_KEY = object : CreationExtras.Key<StatisticsDatabase> {}

            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val statisticsDatabase = (this[STATISTICS_DATABASE_KEY] as StatisticsDatabase)
                    MenuScreenViewModel(statisticsDatabase)
                }
            }
        }
    }