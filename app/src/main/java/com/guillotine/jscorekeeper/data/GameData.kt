package com.guillotine.jscorekeeper.data

import android.os.Bundle
import androidx.core.os.bundleOf
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
