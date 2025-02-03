package com.guillotine.jscorekeeper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "scores",
    primaryKeys = ["timestamp"],
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["timestamp"],
            childColumns = ["timestamp"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)

// Technically unnecessary and could be computed at runtime but it's probably faster to store these.
data class ScoreEntity (
    val timestamp: Long,
    val score: Int
)