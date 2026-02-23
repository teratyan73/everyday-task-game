package com.example.everydaytaskgame.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everydaytaskgame.config.CharacterConfig
import com.example.everydaytaskgame.data.db.entity.CharacterEntity
import com.example.everydaytaskgame.data.db.entity.TaskEntity
import com.example.everydaytaskgame.data.repository.AppStateRepository
import com.example.everydaytaskgame.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val globalStreak: Int = 0,
    val totalPoints: Int = 0,
    val evolutionStage: Int = 0,
    val evolutionEmoji: String = "🥚",
    val evolutionName: String = "タマゴ",
    val pointsToNext: Int? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val appStateRepository: AppStateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // アプリ起動時の日付リセット処理を実行してから UI の監視を開始する
        viewModelScope.launch {
            handleAppLaunch()
            observeState()
        }
    }

    // ---- アプリ起動時処理 ----

    /**
     * 前回起動日と今日を比較し、日付が変わっていれば以下を実行する:
     *  1. 未完了タスクの streak を 0 にリセット
     *  2. 全タスク完了していれば globalStreak+1、そうでなければ 0 にリセット
     *  3. 全タスクの isCompletedToday を false にリセット
     *  4. キャラクター進化ポイントを再計算
     */
    private suspend fun handleAppLaunch() {
        val today = LocalDate.now().toString()
        val appState = appStateRepository.getOrCreateAppState()

        if (appState.lastLaunchDate == today) {
            // 同日中の再起動 → 何もしない
            _uiState.value = _uiState.value.copy(isLoading = false)
            return
        }

        val allTasks = taskRepository.getAllTasks()

        // 前日に全タスクを完了していたか判定
        val allCompleted = allTasks.isNotEmpty() && allTasks.all { it.isCompletedToday }
        val newGlobalStreak = if (allCompleted) appState.globalStreak + 1 else 0

        // 各タスクの streak をリセット（未完了なら 0、完了済みはそのまま維持）
        allTasks.forEach { task ->
            val newStreak = if (task.isCompletedToday) task.streak else 0
            taskRepository.updateTask(task.copy(streak = newStreak, isCompletedToday = false))
        }

        // AppState を更新
        appStateRepository.updateAppState(
            appState.copy(
                globalStreak = newGlobalStreak,
                lastLaunchDate = today
            )
        )

        // キャラクターポイントを再計算
        val updatedTasks = taskRepository.getAllTasks()
        recalculateCharacter(updatedTasks, newGlobalStreak)

        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    // ---- リアルタイム観測 ----

    private suspend fun observeState() {
        combine(
            taskRepository.observeAllTasks(),
            appStateRepository.observeAppState(),
            appStateRepository.observeCharacter()
        ) { tasks, appState, character ->
            val globalStreak = appState?.globalStreak ?: 0
            val totalPoints = character?.totalPoints ?: 0
            val stage = character?.evolutionStage ?: 0
            val threshold = CharacterConfig.getThreshold(stage)
            HomeUiState(
                tasks = tasks,
                globalStreak = globalStreak,
                totalPoints = totalPoints,
                evolutionStage = stage,
                evolutionEmoji = threshold.emoji,
                evolutionName = threshold.nameJa,
                pointsToNext = CharacterConfig.pointsToNextStage(totalPoints),
                isLoading = false
            )
        }.collect { state ->
            _uiState.value = state
        }
    }

    // ---- ユーザー操作 ----

    /**
     * タスクのチェック状態を切り替える。
     * チェック時: isCompletedToday=true, streak+1
     * アンチェック時: isCompletedToday=false, streak-1（最小 0）
     */
    fun toggleTask(taskId: Int) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId) ?: return@launch
            val checking = !task.isCompletedToday
            val newStreak = if (checking) task.streak + 1 else maxOf(0, task.streak - 1)

            taskRepository.updateTask(
                task.copy(isCompletedToday = checking, streak = newStreak)
            )

            taskRepository.recordCompletion(
                taskId = taskId,
                date = LocalDate.now().toString(),
                completed = checking
            )

            // キャラクターポイントを再計算
            val allTasks = taskRepository.getAllTasks()
            val appState = appStateRepository.getOrCreateAppState()
            recalculateCharacter(allTasks, appState.globalStreak)
        }
    }

    /**
     * タスクを削除する。
     */
    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
            val allTasks = taskRepository.getAllTasks()
            val appState = appStateRepository.getOrCreateAppState()
            recalculateCharacter(allTasks, appState.globalStreak)
        }
    }

    // ---- キャラクター進化計算 ----

    /**
     * 総合ポイント = 個々の streak 合計 + globalStreak
     * 総合ポイントから進化段階を決定してDBに保存する。
     */
    private suspend fun recalculateCharacter(tasks: List<TaskEntity>, globalStreak: Int) {
        val totalPoints = tasks.sumOf { it.streak } + globalStreak
        val evolutionStage = CharacterConfig.getEvolutionStage(totalPoints)
        appStateRepository.updateCharacter(
            CharacterEntity(id = 1, evolutionStage = evolutionStage, totalPoints = totalPoints)
        )
    }
}
