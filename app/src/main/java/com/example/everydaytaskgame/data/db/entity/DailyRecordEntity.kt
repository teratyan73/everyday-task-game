package com.example.everydaytaskgame.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 日別タスク完了記録エンティティ。
 * タスクごとの日別履歴を保持します（フェーズ2以降の分析用途を見据えて定義）。
 *
 * @param id 主キー（自動採番）
 * @param taskId 対象タスクの ID（tasks テーブルの外部キー）
 * @param date 記録日（ISO 8601 形式 "YYYY-MM-DD"）
 * @param completed その日にタスクを完了したかどうか
 */
@Entity(
    tableName = "daily_records",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["taskId", "date"], unique = true)]
)
data class DailyRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val date: String,
    val completed: Boolean
)
