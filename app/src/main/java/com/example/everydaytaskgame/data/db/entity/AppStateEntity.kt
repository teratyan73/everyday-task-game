package com.example.everydaytaskgame.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * アプリ全体の状態エンティティ（常に id=1 のシングルトンレコード）。
 *
 * @param id 主キー（固定値 1）
 * @param globalStreak 全タスクを連続して完了した日数（全体ストリーク）
 * @param lastLaunchDate 最終起動日（ISO 8601 形式 "YYYY-MM-DD"、初回は空文字列）
 * @param gemCount ジェム所持数（フェーズ2で使用予定）
 * @param hasGuard ストリーク保護シールドの有無（フェーズ2で使用予定）
 * @param canRevive ストリーク復活権の有無（フェーズ2で使用予定）
 */
@Entity(tableName = "app_state")
data class AppStateEntity(
    @PrimaryKey
    val id: Int = 1,
    val globalStreak: Int = 0,
    val lastLaunchDate: String = "",
    // --- フェーズ2用フィールド ---
    val gemCount: Int = 0,
    val hasGuard: Boolean = false,
    val canRevive: Boolean = false
)
