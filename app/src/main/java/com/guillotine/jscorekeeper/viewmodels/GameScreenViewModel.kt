package com.guillotine.jscorekeeper.viewmodels

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

    val currency = gameData.currency
    private val multipliers = gameData.multipliers
    private val baseMoneyValues = gameData.moneyValues
    private val columns = gameData.columns

    fun dailyDoubleScoreChange(wager: Int) {
        score += wager
        clueDialogState = ClueDialogState.NONE
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

    fun isRemainingDailyDouble(): Boolean {
        return true
    }

    fun showRoundDialog() {
        isShowRoundDialog = true
    }

    fun showClueDialog(value: Int) {
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
        // map returns a List rather than an IntArray. Must convert.
        moneyValues = baseMoneyValues.map { it * multipliers[round] }.toIntArray()
        isShowRoundDialog = false
    }

    fun onCorrectResponse(value: Int) {
        score += value
        onClueDialogDismiss()
    }

    fun onIncorrectResponse(value: Int) {
        score -= value
        onClueDialogDismiss()
    }

    fun onPass(value: Int) {
        onClueDialogDismiss()
    }

    fun onDailyDouble() {
        clueDialogState = ClueDialogState.DAILY_DOUBLE_WAGER
    }

}