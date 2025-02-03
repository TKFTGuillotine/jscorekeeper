package com.guillotine.jscorekeeper.data

import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class SavedGame(
    val gameData: GameData,
    val savedMoneyValues: IntArray,
    val score: Int,
    val round: Int,
    val columnsPerValue: MutableMap<Int, Int>,
    val remainingDailyDoubles: Int,
    val isFinal: Boolean,
    val clueIndex: Int,
    //val timestamp: Long
)

object SavedGameSerializer : Serializer<SavedGame> {
    override val defaultValue: SavedGame
        get() = SavedGame(
            gameData = GameData(
                moneyValues = intArrayOf(0),
                multipliers = intArrayOf(0),
                currency = "",
                // This will never be the case in a real game, so this being a default value
                // indicates there is no saved game to the menu screen.
                columns = 0,
            ),
            savedMoneyValues = intArrayOf(0),
            score = 0,
            round = 0,
            columnsPerValue = mutableMapOf<Int, Int>().toMutableMap(),
            remainingDailyDoubles = 0,
            isFinal = false,
            clueIndex = 0,
            //timestamp = 0
            )

    override suspend fun readFrom(input: InputStream): SavedGame {
        return try {
            Json.decodeFromString(
                deserializer = SavedGame.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            println("Exception in SavedGameSerializer as follows:\n${e}\nReturning default value.")
            defaultValue
        }
    }

    override suspend fun writeTo(t: SavedGame, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = SavedGame.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}