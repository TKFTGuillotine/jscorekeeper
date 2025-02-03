package com.guillotine.jscorekeeper.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.integerResource
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
import com.guillotine.jscorekeeper.R
import com.guillotine.jscorekeeper.data.GameData
import com.guillotine.jscorekeeper.data.GameModes
import com.guillotine.jscorekeeper.database.ClueEntity
import com.guillotine.jscorekeeper.database.ClueType
import com.guillotine.jscorekeeper.database.DailyDoubleEntity
import com.guillotine.jscorekeeper.database.FinalEntity
import com.guillotine.jscorekeeper.database.StatisticsDao
import com.guillotine.jscorekeeper.database.StatisticsDatabase
import kotlinx.coroutines.launch

@OptIn(SavedStateHandleSaveableApi::class)
class ResultsScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val timestamp: Long,
    private val moneyValues: IntArray,
    private val multipliers: IntArray,
    private val currency: String,
    private val score: Int,
    private val columns: Int,
    private val statisticsDatabase: StatisticsDatabase
) : ViewModel() {
    /* Mutable outer list messes with mutableStateOf. Immutable inner list makes this nonfunctional.
       mutableStateListOf appears to be undelegatable somehow. This is the best solution I've got.
     */
    var cluesByRound by savedStateHandle.saveable {mutableStateOf(listOf<MutableList<MutableList<ClueType>>>())}
        private set
    var dailyDoubles by savedStateHandle.saveable {mutableStateOf(listOf<DailyDoubleEntity>())}
        private set
    var finalEntity by savedStateHandle.saveable {mutableStateOf<FinalEntity?>(null)}
        private set
    init {
        viewModelScope.launch {
            var mutableList: MutableList<MutableList<MutableList<ClueType>>> = mutableListOf()
            /* Initialize clues list to be prepared for append. -1 because size will go one past the
               last index, -1 again because the last index is 0 for Final and we want to treat that
               differently. */
            for (round in 0..(multipliers.size - 2)) {
                mutableList.add(mutableListOf())
                for (value in 0..(moneyValues.size - 1)) {
                    mutableList[round].add(mutableListOf())
                }
            }
            val clues = statisticsDatabase.statisticsDao().getClues(timestamp)
            for (clue in clues) {
                mutableList[clue.round][moneyValues.indexOf(clue.value / multipliers[clue.round])].add(clue.type)
            }
            // All non-entries are passes.
            for (round in 0..multipliers.size - 2) {
                for (value in 0..moneyValues.size - 1) {
                    var cluesInValue = mutableList[round][value].size
                    do {
                        cluesInValue++
                        mutableList[round][value].add(ClueType.PASS)
                    } while (cluesInValue < columns)
                }
            }

            // The state only changes if the outer container changes, so this changes the state.
            cluesByRound = mutableList

            dailyDoubles = statisticsDatabase.statisticsDao().getDailyDoubles(timestamp)
            finalEntity = statisticsDatabase.statisticsDao().getFinal(timestamp)
        }
    }

    fun getValues(): IntArray {
        return moneyValues
    }

    fun getMultipliers(): IntArray {
        return multipliers
    }

    fun getCurrency(): String {
        return currency
    }

    companion object {
        val TIMESTAMP_KEY = object: CreationExtras.Key<Long> {}
        val SCORE_KEY = object: CreationExtras.Key<Int> {}
        val MONEY_VALUES_KEY = object: CreationExtras.Key<IntArray> {}
        val MULTIPLIERS_KEY = object: CreationExtras.Key<IntArray> {}
        val CURRENCY_KEY = object: CreationExtras.Key<String> {}
        val COLUMNS_KEY = object: CreationExtras.Key<Int> {}
        val DATABASE_KEY = object: CreationExtras.Key<StatisticsDatabase> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val timestamp = (this[TIMESTAMP_KEY] as Long)
                val score = (this[SCORE_KEY] as Int)
                val moneyValues = (this[MONEY_VALUES_KEY] as IntArray)
                val multipliers = (this[MULTIPLIERS_KEY] as IntArray)
                val currency = (this[CURRENCY_KEY] as String)
                val columns = (this[COLUMNS_KEY] as Int)
                val statisticsDatabase = (this[DATABASE_KEY] as StatisticsDatabase)
                ResultsScreenViewModel(savedStateHandle, timestamp, moneyValues, multipliers, currency, score, columns, statisticsDatabase)
            }
        }
    }

}