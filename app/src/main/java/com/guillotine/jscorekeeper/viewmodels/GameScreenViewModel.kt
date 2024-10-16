package com.guillotine.jscorekeeper.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.guillotine.jscorekeeper.GameData
import com.guillotine.jscorekeeper.GameModes
import com.guillotine.jscorekeeper.R

class GameScreenViewModel(gameData: GameData, isResumeGame: Boolean): ViewModel() {
    var score by mutableStateOf(0)
        private set
    var round by mutableStateOf(0)
        private set
    var moneyValues by mutableStateOf(gameData.moneyValues)
        private set
    var isShowRoundDialog by mutableStateOf(false)
        private set
    var isShowClueDialog by mutableStateOf(false)
        private set
    var currentValue by mutableStateOf(0)
        private set

    val currency = gameData.currency
    val rounds = gameData.rounds
    private val columns = gameData.columns

    fun changeScore(delta: Int) {
        score += delta
    }

    fun showRoundDialog() {
        isShowRoundDialog = true
    }

    fun showClueDialog(value: Int) {
        currentValue = value
        isShowClueDialog = true
    }

    fun onRoundDialogDismiss() {
        isShowRoundDialog = false
    }

    fun onClueDialogDismiss() {
        isShowClueDialog = false
    }

    fun isDoubleJ(): Boolean {
        return round == rounds - 2
    }

    fun isFinalJ(): Boolean {
        return round == rounds - 1
    }

    fun nextRound() {
        round++
        // Rounds is number of rounds. Round is index starting at 0. Second to last round is Double.
        if (round == rounds - 2) {
            // map returns a List rather than an IntArray. Must convert.
            moneyValues = moneyValues.map{it * 2}.toIntArray()
        }
        isShowRoundDialog = false
    }

    fun correctResponse(value: Int) {
        score += value
        isShowClueDialog = false
    }

    fun incorrectResponse(value: Int) {
        score -= value
        isShowClueDialog = false
    }

    fun noAnswer(value: Int) {
        isShowClueDialog = false
    }

    fun dailyDouble(value: Int) {
        isShowClueDialog = false
    }

}