package com.example.everydaytaskgame.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * キャラクターエンティティ（常に id=1 のシングルトンレコード）。
 *
 * @param id 主キー（固定値 1）
 * @param evolutionStage 現在の進化段階（[com.example.everydaytaskgame.config.CharacterConfig] 参照）
 * @param totalPoints 蓄積された総合ポイント（個々の streak 合計 + globalStreak）
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val id: Int = 1,
    val evolutionStage: Int = 0,
    val totalPoints: Int = 0
)
