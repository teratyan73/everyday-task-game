package com.example.everydaytaskgame.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.everydaytaskgame.data.db.dao.AppStateDao
import com.example.everydaytaskgame.data.db.dao.CharacterDao
import com.example.everydaytaskgame.data.db.dao.DailyRecordDao
import com.example.everydaytaskgame.data.db.dao.TaskDao
import com.example.everydaytaskgame.data.db.entity.AppStateEntity
import com.example.everydaytaskgame.data.db.entity.CharacterEntity
import com.example.everydaytaskgame.data.db.entity.DailyRecordEntity
import com.example.everydaytaskgame.data.db.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        DailyRecordEntity::class,
        CharacterEntity::class,
        AppStateEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun dailyRecordDao(): DailyRecordDao
    abstract fun characterDao(): CharacterDao
    abstract fun appStateDao(): AppStateDao
}
