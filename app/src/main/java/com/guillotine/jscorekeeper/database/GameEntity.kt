package com.guillotine.jscorekeeper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guillotine.jscorekeeper.data.GameModes

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "game_mode") val gameMode: GameModes,
    val visible: Boolean
)
