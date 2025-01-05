package com.guillotine.jscorekeeper.composable.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class, ClueEntity::class, DailyDoubleEntity::class, FinalEntity::class], version = 1)
abstract class StatisticsDatabase: RoomDatabase() {
    abstract fun statisticsDao(): StatisticsDao
}