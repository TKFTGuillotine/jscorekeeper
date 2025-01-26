package com.guillotine.jscorekeeper.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "finals",
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
data class FinalEntity(
    @PrimaryKey val timestamp: Long,
    val wager: Int,
    val correct: Boolean
)
