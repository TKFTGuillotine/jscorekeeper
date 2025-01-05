package com.guillotine.jscorekeeper.composable.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "daily_doubles",
    primaryKeys = ["timestamp", "index"],
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["timestamp"],
            childColumns = ["timestamp"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class DailyDoubleEntity(
    val timestamp: Long,
    val index: Int,
    val wager: Int,
    val round: Int,
    @ColumnInfo(name = "was_correct") val wasCorrect: Boolean
)
