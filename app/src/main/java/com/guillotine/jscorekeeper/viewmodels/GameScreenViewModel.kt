package com.guillotine.jscorekeeper.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.guillotine.jscorekeeper.data.ClueDialogState
import com.guillotine.jscorekeeper.data.GameData

class GameScreenViewModel(gameData: GameData, isResumeGame: Boolean) : ViewModel() {
    var score by mutableStateOf(0)
        private set
    var round by mutableStateOf(0)
        private set
    var moneyValues by mutableStateOf(gameData.moneyValues)
        private set
    var clueDialogState by mutableStateOf(ClueDialogState.NONE)
        private set
    var isShowRoundDialog by mutableStateOf(false)
        private set
    var currentValue by mutableStateOf(0)
        private set
    var snackbarHostState by mutableStateOf(SnackbarHostState())
        private set

    val currency = gameData.currency
    private val multipliers = gameData.multipliers
    private val baseMoneyValues = gameData.moneyValues
    private val columns = gameData.columns

    // Initializes a map inline where each key is associated with columns.
    private var columnsPerValue = moneyValues.associateWith { columns - 1 }.toMutableMap()

    // State for delayed deduction of Daily Double value
    private var isDailyDouble = false
    private var dailyDoubleInitialValue = 0

    // Initialized to first value, will increment later.
    private var remainingDailyDoubles = multipliers[0]

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

    fun isRemainingDailyDouble(): Boolean {
        return remainingDailyDoubles != 0
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
        clueDialogState = ClueDialogState.MAIN
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
        // Refresh number of Daily Doubles
        remainingDailyDoubles = multipliers[round]
        // map returns a List rather than an IntArray. Must convert.
        moneyValues = baseMoneyValues.map { it * multipliers[round] }.toIntArray()
        // Refresh columnsPerValue
        columnsPerValue = moneyValues.associateWith { columns }.toMutableMap()
        isShowRoundDialog = false
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

}