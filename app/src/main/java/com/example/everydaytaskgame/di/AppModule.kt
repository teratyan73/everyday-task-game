package com.example.everydaytaskgame.di

import android.content.Context
import androidx.room.Room
import com.example.everydaytaskgame.data.db.AppDatabase
import com.example.everydaytaskgame.data.db.dao.AppStateDao
import com.example.everydaytaskgame.data.db.dao.CharacterDao
import com.example.everydaytaskgame.data.db.dao.DailyRecordDao
import com.example.everydaytaskgame.data.db.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "habit_task_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideDailyRecordDao(database: AppDatabase): DailyRecordDao = database.dailyRecordDao()

    @Provides
    fun provideCharacterDao(database: AppDatabase): CharacterDao = database.characterDao()

    @Provides
    fun provideAppStateDao(database: AppDatabase): AppStateDao = database.appStateDao()
}
