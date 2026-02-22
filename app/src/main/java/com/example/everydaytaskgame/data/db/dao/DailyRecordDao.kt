package com.example.everydaytaskgame.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.everydaytaskgame.data.db.entity.DailyRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: DailyRecordEntity)

    @Query("SELECT * FROM daily_records WHERE taskId = :taskId ORDER BY date DESC")
    fun observeByTask(taskId: Int): Flow<List<DailyRecordEntity>>

    @Query("SELECT * FROM daily_records WHERE taskId = :taskId AND date = :date LIMIT 1")
    suspend fun getByTaskAndDate(taskId: Int, date: String): DailyRecordEntity?

    @Query("SELECT * FROM daily_records WHERE date = :date")
    suspend fun getByDate(date: String): List<DailyRecordEntity>

    @Query("DELETE FROM daily_records WHERE taskId = :taskId")
    suspend fun deleteByTask(taskId: Int)
}
