package com.example.everydaytaskgame.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.everydaytaskgame.data.db.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(character: CharacterEntity)

    @Query("SELECT * FROM characters WHERE id = 1")
    suspend fun get(): CharacterEntity?

    @Query("SELECT * FROM characters WHERE id = 1")
    fun observe(): Flow<CharacterEntity?>
}
