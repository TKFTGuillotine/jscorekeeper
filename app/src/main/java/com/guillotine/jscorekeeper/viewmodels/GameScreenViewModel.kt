package com.guillotine.jscorekeeper.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameScreenViewModel(rounds: Int): ViewModel() {
    var score by mutableStateOf(0)
        private set
    var round by mutableStateOf(0)
        private set
    var moneyValues by mutableStateOf(IntArray(5))
        private set
    private val rounds = rounds


    fun changeScore(delta: Int) {
        score += delta
    }

    fun nextRound() {
        round++
        // Rounds is number of rounds. Round is index starting at 0. Second to last round is Double.
        if (round == rounds - 2) {
            // map returns a List rather than an IntArray. Must convert.
            moneyValues = moneyValues.map{it * 2}.toIntArray()
        }
    }

}