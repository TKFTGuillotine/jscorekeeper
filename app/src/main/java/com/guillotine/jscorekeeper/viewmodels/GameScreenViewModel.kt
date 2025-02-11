package com.guillotine.jscorekeeper.viewmodels

import android.os.Bundle
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
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
import com.guillotine.jscorekeeper.database.ClueEntity
import com.guillotine.jscorekeeper.database.ClueType
import com.guillotine.jscorekeeper.database.DailyDoubleEntity
import com.guillotine.jscorekeeper.database.StatisticsDatabase
import com.guillotine.jscorekeeper.data.RadioButtonOptions
import com.guillotine.jscorekeeper.data.ClueDialogState
import com.guillotine.jscorekeeper.data.GameData
import com.guillotine.jscorekeeper.data.SavedGame
import kotlinx.coroutines.launch

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
    private val dataStore: DataStore<SavedGame>,
    private val statisticsDatabase: StatisticsDatabase,
    val gameTimestamp: Long
) : ViewModel() {
    private var savedGameData: GameData

    init {
        /* Bundle stuff has nothing to do with resuming a saved game and everything to do with
           restoring state when the app is killed in the background and revisited by the user.
           That's why it's tied into savedStateHandle. It's just dealing with things more complex
           than saveable can manage. */
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
    var moneyValues by savedStateHandle.saveable { mutableStateOf(savedGameData.moneyValues) }
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
        mutableStateOf(RadioButtonOptions.CORRECT)
    }
        private set
    var wagerFieldText by savedStateHandle.saveable { mutableStateOf("") }
    var isShowWagerFieldError by savedStateHandle.saveable { mutableStateOf(false) }

    var currency = savedGameData.currency
    private var multipliers = savedGameData.multipliers
    private var baseMoneyValues = savedGameData.moneyValues
    private var columns = savedGameData.columns

    // This one is a unique data type and so doesn't delegate the same way, you just do an assign.
    var columnsPerValue = mutableStateMapOf<Int, Int>()
        private set

    init {
        val columnsPerValueBundle = savedStateHandle.get<Bundle>("columns_per_value")

        if (columnsPerValueBundle != null) {
            // Docs are a little opaque on this one but putAll is how you assign a regular map.
            columnsPerValue.putAll(columnsPerValueBundle.loadMap())
        } else {
            columnsPerValue.putAll(moneyValues.associateWith { columns }.toMap())
        }
        savedStateHandle.setSavedStateProvider("columns_per_value") {
            columnsPerValue.saveMap()
        }
    }

    // Initializes a counter for persisting the current clue to Room
    private var clueIndex by savedStateHandle.saveable { mutableStateOf(0) }

    // State for delayed deduction of Daily Double value
    private var isDailyDouble: Boolean
    private var dailyDoubleInitialValue: Int

    // Initialized to first value, will increment later.
    private var remainingDailyDoubles: Int

    // Used for transition out of game, otherwise the death of the ViewModel assumes we need to save
    // and resume the game later.
    private var isGameComplete: Boolean = false

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

    var isFinal by savedStateHandle.saveable { mutableStateOf(false) }

    // Resume game code.
    init {
        // If we're resuming a game, we'll know, because no game can be played with no columns.
        if (gameData.columns == 0) {
            viewModelScope.launch {
                dataStore.data.collect {
                    savedGameData = it.gameData
                    score = it.score
                    round = it.round
                    moneyValues = it.savedMoneyValues
                    baseMoneyValues = it.gameData.moneyValues
                    columnsPerValue.putAll(it.columnsPerValue)
                    remainingDailyDoubles = it.remainingDailyDoubles
                    isFinal = it.isFinal
                    clueIndex = it.clueIndex

                    multipliers = savedGameData.multipliers
                    currency = savedGameData.currency
                    columns = savedGameData.columns
                }
            }
        }
        // If not, we need to initialize a new game as the saved game.
        else {
            viewModelScope.launch {
                saveGame()
            }
        }
    }

    fun getMultipliers(): IntArray {
        return multipliers
    }

    fun getBaseMoneyValues(): IntArray {
        return baseMoneyValues
    }

    fun getColumns(): Int {
        return columns
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
        // Transition to Final J!
        if (multipliers[round + 1] == 0) {
            isFinal = true
            isShowRoundDialog = false
        } else {
            // Since this is going to trigger a screen transition, it will look weird if the header
            // bar changes while the screen is transitioning instead of landing on the new title.
            round++
            // Refresh number of Daily Doubles
            remainingDailyDoubles = multipliers[round]
            // map returns a List rather than an IntArray. Must convert.
            moneyValues = baseMoneyValues.map { it * multipliers[round] }.toIntArray()
            // Refresh columnsPerValue
            columnsPerValue.putAll( moneyValues.associateWith { columns }.toMap())
            isShowRoundDialog = false
        }
        saveGame()
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
            // Persist to stats database
            viewModelScope.launch {
                statisticsDatabase.statisticsDao().insertClue(
                    ClueEntity(
                        timestamp = gameTimestamp,
                        value = dailyDoubleInitialValue,
                        type = ClueType.DAILY_DOUBLE,
                        round = round,
                        index = clueIndex
                    )
                )
                statisticsDatabase.statisticsDao().insertDailyDouble(
                    DailyDoubleEntity(
                        timestamp = gameTimestamp,
                        index = clueIndex++,
                        wager = value,
                        round = round,
                        wasCorrect = true
                    )
                )
            }

        } else {
            if (columnsPerValue[value] != null) {
                columnsPerValue[value] = columnsPerValue[value]!! - 1
            }
            // Persist to stats database
            viewModelScope.launch {
                statisticsDatabase.statisticsDao().insertClue(
                    ClueEntity(
                        timestamp = gameTimestamp,
                        value = value,
                        type = ClueType.CORRECT,
                        round = round,
                        index = clueIndex++
                    )
                )
            }
        }
        onClueDialogDismiss()
        saveGame()
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
            viewModelScope.launch {
                // Persist to stats database
                statisticsDatabase.statisticsDao().insertClue(
                    ClueEntity(
                        timestamp = gameTimestamp,
                        value = dailyDoubleInitialValue,
                        type = ClueType.DAILY_DOUBLE,
                        round = round,
                        index = clueIndex
                    )
                )
                statisticsDatabase.statisticsDao().insertDailyDouble(
                    DailyDoubleEntity(
                        timestamp = gameTimestamp,
                        index = clueIndex++,
                        wager = value,
                        round = round,
                        wasCorrect = false
                    )
                )
            }
        } else {
            if (columnsPerValue[value] != null) {
                columnsPerValue[value] = columnsPerValue[value]!! - 1
            }
            viewModelScope.launch {
                // Persist to stats database
                statisticsDatabase.statisticsDao().insertClue(
                    ClueEntity(
                        timestamp = gameTimestamp,
                        value = value,
                        type = ClueType.INCORRECT,
                        round = round,
                        index = clueIndex++
                    )
                )
            }
        }
        onClueDialogDismiss()
        saveGame()
        return (isDailyDouble && remainingDailyDoubles != 0)
    }

    fun onPass(value: Int) {
        if (columnsPerValue[value] != null) {
            columnsPerValue[value] = columnsPerValue[value]!! - 1
        }
        viewModelScope.launch {
            // Persist to stats database
            statisticsDatabase.statisticsDao().insertClue(
                ClueEntity(
                    timestamp = gameTimestamp,
                    value = value,
                    type = ClueType.PASS,
                    round = round,
                    index = clueIndex++
                )
            )
        }
        onClueDialogDismiss()
        saveGame()
    }

    fun onDailyDouble() {
        isDailyDouble = true
        dailyDoubleInitialValue = currentValue
        clueDialogState = ClueDialogState.DAILY_DOUBLE_WAGER
        // Since this will show up in just a moment, reset it to something that will be visible.
        currentSelectedClueDialogOption = RadioButtonOptions.CORRECT
    }

    /* Should be called constantly after every action taken in the game to ensure it is constantly
       saved properly
     */
    private fun saveGame() {
        viewModelScope.launch {
            dataStore.updateData {
                it.copy(
                    gameData = savedGameData,
                    savedMoneyValues = moneyValues,
                    score = score,
                    round = round,
                    columnsPerValue = columnsPerValue,
                    remainingDailyDoubles = remainingDailyDoubles,
                    isFinal = isFinal,
                    clueIndex = clueIndex
                )
            }
        }
    }

    // Will get called upon to create this ViewModel in the Activity to ensure the SavedStateHandle
    // exists and can do its job, rather than handling ViewModel creation in the Composable.
    companion object {
        val GAME_DATA_KEY = object : CreationExtras.Key<GameData> {}
        val DATA_STORE_KEY = object : CreationExtras.Key<DataStore<SavedGame>> {}
        val STATISTICS_DATABASE_KEY = object : CreationExtras.Key<StatisticsDatabase> {}
        val GAME_TIMESTAMP_KEY = object : CreationExtras.Key<Long> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val gameData = (this[GAME_DATA_KEY] as GameData)
                val dataStore = (this[DATA_STORE_KEY] as DataStore<SavedGame>)
                val statisticsDatabase = (this[STATISTICS_DATABASE_KEY] as StatisticsDatabase)
                val gameTimestamp = (this[GAME_TIMESTAMP_KEY] as Long)
                GameScreenViewModel(
                    savedStateHandle,
                    gameData,
                    dataStore,
                    statisticsDatabase,
                    gameTimestamp
                )
            }
        }
    }

}