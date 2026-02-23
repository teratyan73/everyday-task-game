package com.example.everydaytaskgame.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.everydaytaskgame.data.db.entity.AppStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(appState: AppStateEntity)

    @Query("SELECT * FROM app_state WHERE id = 1")
    suspend fun get(): AppStateEntity?

    @Query("SELECT * FROM app_state WHERE id = 1")
    fun observe(): Flow<AppStateEntity?>
}
