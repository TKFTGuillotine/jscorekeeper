package com.guillotine.jscorekeeper.composable.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "clues",
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
data class ClueEntity(
    val timestamp: Long,
    val round: Int,
    val index: Int,
    val value: Int,
    val type: ClueType
)
