package com.example.everydaytaskgame.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 習慣タスクエンティティ。
 *
 * @param id 主キー（自動採番）
 * @param name タスク名
 * @param streak 現在の連続達成日数
 * @param isCompletedToday 今日チェック済みかどうか
 * @param createdAt 作成日時（エポックミリ秒）
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val streak: Int = 0,
    val isCompletedToday: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
