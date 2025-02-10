package com.guillotine.jscorekeeper.data

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.guillotine.jscorekeeper.R
import kotlinx.serialization.Serializable

@Serializable
data class GameData(
    val moneyValues: IntArray,
    val multipliers: IntArray,
    val currency: String,
    val columns: Int
) {
    fun saveGameData(): Bundle {
        return bundleOf(
            "moneyValues" to moneyValues,
            "multipliers" to multipliers,
            "currency" to currency,
            "columns" to columns
        )
    }
}


fun processGameData(applicationContext: Context, gameMode: GameModes): GameData {
    val rounds = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getIntArray(R.array.usa_round_multiplier)
        GameModes.UK -> applicationContext.resources.getIntArray(R.array.uk_round_multiplier)
        GameModes.AUSTRALIA -> applicationContext.resources.getIntArray(R.array.australia_round_multiplier)
        GameModes.RESUME -> applicationContext.resources.getIntArray(R.array.resume_round_multiplier)
    }
    val columns = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getInteger(R.integer.usa_columns)
        GameModes.UK -> applicationContext.resources.getInteger(R.integer.uk_columns)
        GameModes.AUSTRALIA -> applicationContext.resources.getInteger(R.integer.australia_columns)
        GameModes.RESUME -> applicationContext.resources.getInteger(R.integer.resume_columns)
    }
    val moneyValues = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getIntArray(R.array.usa_money)
        GameModes.UK -> applicationContext.resources.getIntArray(R.array.uk_money)
        GameModes.AUSTRALIA -> applicationContext.resources.getIntArray(R.array.australia_money)
        GameModes.RESUME -> applicationContext.resources.getIntArray(R.array.resume_money)
    }
    val currency = when (gameMode) {
        GameModes.USA -> applicationContext.resources.getString(R.string.usa_currency)
        GameModes.UK -> applicationContext.resources.getString(R.string.uk_currency)
        GameModes.AUSTRALIA -> applicationContext.resources.getString(R.string.australia_currency)
        GameModes.RESUME -> applicationContext.resources.getString(R.string.resume_currency)
    }

    return GameData(moneyValues, rounds, currency, columns)
}