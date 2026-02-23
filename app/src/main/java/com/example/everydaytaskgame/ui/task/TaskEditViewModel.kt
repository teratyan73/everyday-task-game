package com.example.everydaytaskgame.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everydaytaskgame.data.db.entity.TaskEntity
import com.example.everydaytaskgame.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskEditUiState(
    val taskName: String = "",
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isDone: Boolean = false,
    val nameError: String? = null
)

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskEditUiState())
    val uiState: StateFlow<TaskEditUiState> = _uiState.asStateFlow()

    private var currentTaskId: Int = -1

    /**
     * 編集対象のタスクを読み込む。
     * taskId が -1 の場合は新規追加モード。
     */
    fun loadTask(taskId: Int) {
        currentTaskId = taskId
        if (taskId == -1) {
            _uiState.value = TaskEditUiState(isEditMode = false)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val task = taskRepository.getTaskById(taskId)
            _uiState.value = TaskEditUiState(
                taskName = task?.name ?: "",
                isEditMode = task != null,
                isLoading = false
            )
        }
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(
            taskName = name,
            nameError = null
        )
    }

    /**
     * タスクを保存する。
     * 新規の場合は INSERT、既存の場合は名前のみ UPDATE する。
     */
    fun saveTask() {
        val name = _uiState.value.taskName.trim()
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "タスク名を入力してください")
            return
        }

        viewModelScope.launch {
            if (currentTaskId == -1) {
                taskRepository.insertTask(
                    TaskEntity(name = name)
                )
            } else {
                val existing = taskRepository.getTaskById(currentTaskId) ?: return@launch
                taskRepository.updateTask(existing.copy(name = name))
            }
            _uiState.value = _uiState.value.copy(isDone = true)
        }
    }

    /**
     * タスクを削除する（編集モードのみ）。
     */
    fun deleteTask() {
        if (currentTaskId == -1) return
        viewModelScope.launch {
            taskRepository.deleteTask(currentTaskId)
            _uiState.value = _uiState.value.copy(isDone = true)
        }
    }
}
