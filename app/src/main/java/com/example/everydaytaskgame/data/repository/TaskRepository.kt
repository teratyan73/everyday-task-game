package com.example.everydaytaskgame.data.repository

import com.example.everydaytaskgame.data.db.dao.DailyRecordDao
import com.example.everydaytaskgame.data.db.dao.TaskDao
import com.example.everydaytaskgame.data.db.entity.DailyRecordEntity
import com.example.everydaytaskgame.data.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val dailyRecordDao: DailyRecordDao
) {

    // ---- タスク操作 ----

    fun observeAllTasks(): Flow<List<TaskEntity>> = taskDao.observeAll()

    suspend fun getAllTasks(): List<TaskEntity> = taskDao.getAll()

    suspend fun getTaskById(id: Int): TaskEntity? = taskDao.getById(id)

    suspend fun insertTask(task: TaskEntity): Long = taskDao.insert(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.update(task)

    suspend fun deleteTask(taskId: Int) {
        taskDao.deleteById(taskId)
        // CASCADE 設定により daily_records も自動削除されます
    }

    // ---- 日別記録操作 ----

    /**
     * 指定タスクの指定日の完了を記録します。
     * 同一タスク・同一日の記録が存在する場合は上書きします（REPLACE）。
     */
    suspend fun recordCompletion(taskId: Int, date: String, completed: Boolean = true) {
        dailyRecordDao.insert(
            DailyRecordEntity(
                taskId = taskId,
                date = date,
                completed = completed
            )
        )
    }

    fun observeRecordsForTask(taskId: Int) = dailyRecordDao.observeByTask(taskId)
}
