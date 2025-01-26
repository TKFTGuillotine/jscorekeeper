package com.guillotine.jscorekeeper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StatisticsDao {
    @Query("SELECT * FROM games WHERE visible=1")
    suspend fun getFinishedGames(): List<GameEntity>

    @Query("SELECT * FROM games WHERE visible=0 LIMIT 1")
    suspend fun getSavedGame(): GameEntity?

    @Query("DELETE FROM games WHERE visible=0")
    suspend fun deleteSavedGame()

    @Query("UPDATE games SET visible=1 WHERE timestamp=:timestamp")
    suspend fun setVisible(timestamp: Long)

    @Insert
    suspend fun createGame(game: GameEntity)

    @Insert
    suspend fun insertClue(clue: ClueEntity)

    @Insert
    suspend fun insertDailyDouble(dailyDouble: DailyDoubleEntity)

    @Insert
    // can't call it final haha
    suspend fun insertFinal(finalClue: FinalEntity)
}