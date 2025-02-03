package com.guillotine.jscorekeeper.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class, ClueEntity::class, DailyDoubleEntity::class, FinalEntity::class, ScoreEntity::class], version = 2)
abstract class StatisticsDatabase: RoomDatabase() {
    abstract fun statisticsDao(): StatisticsDao
}