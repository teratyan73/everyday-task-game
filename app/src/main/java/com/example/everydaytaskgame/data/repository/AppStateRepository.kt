package com.example.everydaytaskgame.data.repository

import com.example.everydaytaskgame.data.db.dao.AppStateDao
import com.example.everydaytaskgame.data.db.dao.CharacterDao
import com.example.everydaytaskgame.data.db.entity.AppStateEntity
import com.example.everydaytaskgame.data.db.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateRepository @Inject constructor(
    private val appStateDao: AppStateDao,
    private val characterDao: CharacterDao
) {

    // ---- AppState ----

    fun observeAppState(): Flow<AppStateEntity?> = appStateDao.observe()

    /**
     * AppState を取得します。レコードが存在しない場合はデフォルト値で作成します。
     */
    suspend fun getOrCreateAppState(): AppStateEntity {
        return appStateDao.get() ?: AppStateEntity().also { appStateDao.upsert(it) }
    }

    suspend fun updateAppState(appState: AppStateEntity) = appStateDao.upsert(appState)

    // ---- Character ----

    fun observeCharacter(): Flow<CharacterEntity?> = characterDao.observe()

    /**
     * Character を取得します。レコードが存在しない場合はデフォルト値で作成します。
     */
    suspend fun getOrCreateCharacter(): CharacterEntity {
        return characterDao.get() ?: CharacterEntity().also { characterDao.upsert(it) }
    }

    suspend fun updateCharacter(character: CharacterEntity) = characterDao.upsert(character)
}
