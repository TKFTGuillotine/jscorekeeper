package com.guillotine.jscorekeeper.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guillotine.jscorekeeper.composable.database.FinalEntity
import com.guillotine.jscorekeeper.composable.database.StatisticsDatabase
import com.guillotine.jscorekeeper.data.RadioButtonOptions
import kotlinx.coroutines.launch

@OptIn(SavedStateHandleSaveableApi::class)
class FinalScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val statisticsDatabase: StatisticsDatabase,
    private val gameTimestamp: Long
) : ViewModel() {
    var wagerText by savedStateHandle.saveable { mutableStateOf("") }
    var isShowError by savedStateHandle.saveable { mutableStateOf(false) }
        private set
    var isGameComplete by savedStateHandle.saveable { mutableStateOf(false) }
        private set
    var currentSelectedRadioButton by savedStateHandle.saveable { mutableStateOf(RadioButtonOptions.CORRECT) }
        private set
    var round by savedStateHandle.saveable { mutableStateOf(0) }
    var score by savedStateHandle.saveable { mutableStateOf(0) }
    var currency by savedStateHandle.saveable { mutableStateOf("$") }

    fun submitFinalWager(wager: Int, score: Int, isCorrect: Boolean): Int? {
        if ((wager in 0..score) || wager == 0) {
            viewModelScope.launch {
                statisticsDatabase.statisticsDao().insertFinal(
                    FinalEntity(
                        timestamp = gameTimestamp,
                        wager = wager,
                        correct = isCorrect
                    )
                )
                statisticsDatabase.statisticsDao().setVisible(gameTimestamp)
            }
            if (isCorrect) {
                isGameComplete = true
                return score + wager
            } else {
                isGameComplete = true
                return score - wager
            }
        }
        isShowError = true
        return null
    }

    fun onRadioButtonSelected(button: RadioButtonOptions) {
        currentSelectedRadioButton = button
    }

    companion object {
        val STATISTICS_DATABASE_KEY = object : CreationExtras.Key<StatisticsDatabase> {}
        val GAME_TIMESTAMP_KEY = object : CreationExtras.Key<Long> {}
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val statisticsDatabase = (this[STATISTICS_DATABASE_KEY] as StatisticsDatabase)
                val gameTimestamp = (this[GAME_TIMESTAMP_KEY] as Long)
                FinalScreenViewModel(
                    savedStateHandle,
                    statisticsDatabase,
                    gameTimestamp
                )
            }
        }
    }
}