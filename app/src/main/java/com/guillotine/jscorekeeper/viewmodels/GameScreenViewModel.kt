package com.guillotine.jscorekeeper.viewmodels

import android.os.Bundle
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guillotine.jscorekeeper.data.RadioButtonOptions
import com.guillotine.jscorekeeper.data.ClueDialogState
import com.guillotine.jscorekeeper.data.GameData

private fun MutableMap<Int, Int>.saveMap(): Bundle {
    val bundle = Bundle()
    for (entry in this) {
        bundle.putInt(entry.key.toString(), entry.value)
    }
    return bundle
}

private fun Bundle.loadMap(): MutableMap<Int, Int> {
    val map = mutableMapOf<Int, Int>()
    for (key in this.keySet()) {
        map[key.toInt()] = this.getInt(key)
    }
    return map
}

@OptIn(SavedStateHandleSaveableApi::class)
class GameScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val gameData: GameData,
    private val isResumeGame: Boolean
) : ViewModel() {
    private val savedGameData: GameData

    init {
        val gameDataBundle = savedStateHandle.get<Bundle>("game_data")
        savedGameData =
            if (gameDataBundle != null) {
                GameData(
                    gameDataBundle.getIntArray("moneyValues")!!,
                    gameDataBundle.getIntArray("multipliers")!!,
                    gameDataBundle.getString("currency")!!,
                    gameDataBundle.getInt("columns")
                )
            } else {
                gameData
            }
        savedStateHandle.setSavedStateProvider("game_data") {
            savedGameData.saveGameData()
        }
    }

    var score by savedStateHandle.saveable { mutableIntStateOf(0) }
        private set
    var round by savedStateHandle.saveable { mutableIntStateOf(0) }
        private set

    // This is saveable so theoretically it shouldn't matter that it's pulling from gameData, and if
    // it does, pulling from savedGameData wouldn't solve that problem because this can be modified
    // and that should not be.
    var moneyValues by savedStateHandle.saveable { mutableStateOf(gameData.moneyValues) }
        private set
    var clueDialogState by savedStateHandle.saveable { mutableStateOf(ClueDialogState.NONE) }
        private set
    var isShowRoundDialog by savedStateHandle.saveable { mutableStateOf(false) }
        private set
    var currentValue by savedStateHandle.saveable { mutableIntStateOf(0) }
        private set

    // Doesn't really need to be persisted. If the app has been cleaned from memory, the Snackbar
    // will have been long past gone anyways.
    var snackbarHostState by mutableStateOf(SnackbarHostState())
        private set
    var currentSelectedClueDialogOption by savedStateHandle.saveable {
        mutableStateOf(
            RadioButtonOptions.CORRECT
        )
    }
        private set
    var wagerFieldText by savedStateHandle.saveable { mutableStateOf("") }
    var isShowWagerFieldError by savedStateHandle.saveable { mutableStateOf(false) }
    var isFinal by savedStateHandle.saveable { mutableStateOf(false) }

    val currency = savedGameData.currency
    private val multipliers = savedGameData.multipliers
    private val baseMoneyValues = savedGameData.moneyValues
    private val columns = savedGameData.columns

    // Initializes a map inline where each key is associated with columns.
    private var columnsPerValue: MutableMap<Int, Int>

    init {
        val columnsPerValueBundle = savedStateHandle.get<Bundle>("columns_per_value")
        columnsPerValue =
            if (columnsPerValueBundle != null) {
                columnsPerValueBundle.loadMap()
            } else {
                moneyValues.associateWith { columns - 1 }.toMutableMap()
            }
        savedStateHandle.setSavedStateProvider("columns_per_value") {
            columnsPerValue.saveMap()
        }
    }

    // State for delayed deduction of Daily Double value
    private var isDailyDouble: Boolean
    private var dailyDoubleInitialValue: Int

    // Initialized to first value, will increment later.
    private var remainingDailyDoubles: Int

    init {
        val dailyDoubleInfoBundle = savedStateHandle.get<Bundle>("daily_double_info")
        if (dailyDoubleInfoBundle != null) {
            isDailyDouble = dailyDoubleInfoBundle.getBoolean("isDailyDouble")
            dailyDoubleInitialValue = dailyDoubleInfoBundle.getInt("dailyDoubleInitialValue")
            remainingDailyDoubles = dailyDoubleInfoBundle.getInt("remainingDailyDoubles")
        } else {
            isDailyDouble = false
            dailyDoubleInitialValue = 0
            remainingDailyDoubles = multipliers[0]
        }
        savedStateHandle.setSavedStateProvider("daily_double_info") {
            bundleOf(
                "isDailyDouble" to isDailyDouble,
                "dailyDoubleInitialValue" to dailyDoubleInitialValue,
                "remainingDailyDoubles" to remainingDailyDoubles
            )
        }
    }


    fun isWagerValid(wager: Int): Boolean {
        if (wager > score && wager > moneyValues.max()) {
            return false
        } else if (wager < 5) {
            return false
        } else {
            clueDialogState = ClueDialogState.DAILY_DOUBLE_RESPONSE
            currentValue = wager
            return true
        }
    }

    fun getClueDialogOptions(): List<RadioButtonOptions> {
        if (remainingDailyDoubles != 0) {
            return listOf(
                RadioButtonOptions.CORRECT,
                RadioButtonOptions.INCORRECT,
                RadioButtonOptions.DAILY_DOUBLE,
                RadioButtonOptions.PASS
            )
        }
        return listOf(
            RadioButtonOptions.CORRECT,
            RadioButtonOptions.INCORRECT,
            RadioButtonOptions.PASS
        )
    }

    fun isValueRemaining(value: Int): Boolean {
        return columnsPerValue[value] != 0
    }

    fun showRoundDialog() {
        isShowRoundDialog = true
    }

    fun showClueDialog(value: Int) {
        // For tracking internal state.
        isDailyDouble = false
        currentValue = value
        currentSelectedClueDialogOption = RadioButtonOptions.CORRECT
        clueDialogState = ClueDialogState.MAIN
        wagerFieldText = ""
        isShowWagerFieldError = false
    }

    fun onClueDialogOptionSelected(option: RadioButtonOptions) {
        currentSelectedClueDialogOption = option
    }

    fun onRoundDialogDismiss() {
        isShowRoundDialog = false
    }

    fun onClueDialogDismiss() {
        clueDialogState = ClueDialogState.NONE
    }

    fun getMultiplier(): Int {
        return multipliers[round]
    }

    fun nextRound() {
        round++
        // Transition to Final J!
        if (multipliers[round] == 0) {
            isFinal = true
            isShowRoundDialog = false
        } else {
            // Refresh number of Daily Doubles
            remainingDailyDoubles = multipliers[round]
            // map returns a List rather than an IntArray. Must convert.
            moneyValues = baseMoneyValues.map { it * multipliers[round] }.toIntArray()
            // Refresh columnsPerValue
            columnsPerValue = moneyValues.associateWith { columns }.toMutableMap()
            isShowRoundDialog = false
        }
    }

    fun onCorrectResponse(value: Int): Boolean {
        score += value
        if (isDailyDouble) {
            remainingDailyDoubles--
            // I feel like there must be an easier way of doing this but this is all I can seem to
            // get to work.
            if (columnsPerValue[dailyDoubleInitialValue] != null) {
                columnsPerValue[dailyDoubleInitialValue] =
                    columnsPerValue[dailyDoubleInitialValue]!! - 1
            }
        } else {
            if (columnsPerValue[value] != null) {
                columnsPerValue[value] = columnsPerValue[value]!! - 1
            }
        }
        onClueDialogDismiss()
        return (isDailyDouble && remainingDailyDoubles != 0)
    }

    fun onIncorrectResponse(value: Int): Boolean {
        score -= value
        if (isDailyDouble) {
            remainingDailyDoubles--
            if (columnsPerValue[dailyDoubleInitialValue] != null) {
                columnsPerValue[dailyDoubleInitialValue] =
                    columnsPerValue[dailyDoubleInitialValue]!! - 1
            }
        } else {
            if (columnsPerValue[value] != null) {
                columnsPerValue[value] = columnsPerValue[value]!! - 1
            }
        }
        onClueDialogDismiss()
        return (isDailyDouble && remainingDailyDoubles != 0)
    }

    fun onPass(value: Int) {
        if (columnsPerValue[value] != null) {
            columnsPerValue[value] = columnsPerValue[value]!! - 1
        }
        onClueDialogDismiss()
    }

    fun onDailyDouble() {
        isDailyDouble = true
        dailyDoubleInitialValue = currentValue
        clueDialogState = ClueDialogState.DAILY_DOUBLE_WAGER
    }

    fun submitFinalWager(wager: Int, isCorrect: Boolean): Int? {
        if ((wager in 0..score) || wager == 0) {
            if (isCorrect) {
                return score + wager
            } else {
                return score - wager
            }
        }
        isShowWagerFieldError = true
        return null
    }

    // Will get called upon to create this ViewModel in the Activity to ensure the SavedStateHandle
    // exists and can do its job, rather than handling ViewModel creation in the Composable.
    companion object {
        val GAME_DATA_KEY = object : CreationExtras.Key<GameData> {}
        val IS_RESUME_GAME_KEY = object : CreationExtras.Key<Boolean> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val gameData = (this[GAME_DATA_KEY] as GameData)
                val isResumeGame = (this[IS_RESUME_GAME_KEY] as Boolean)
                GameScreenViewModel(
                    savedStateHandle,
                    gameData,
                    isResumeGame
                )
            }
        }
    }

}