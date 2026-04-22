package com.joseleandro.fullfocus.ui.screen.pomodoro

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.repository.PomodoroTimeRepository
import com.joseleandro.fullfocus.domain.usecase.GetFilteredTasksUseCase
import com.joseleandro.fullfocus.domain.usecase.GetTaskEndPomodoroSessionUseCase
import com.joseleandro.fullfocus.domain.usecase.SetCurrentTaskPomodoroUseCase
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val repository: PomodoroTimeRepository,
    private val getTaskEndPomodoroSessionUseCase: GetTaskEndPomodoroSessionUseCase,
    private val setCurrentTaskPomodoroUseCase: SetCurrentTaskPomodoroUseCase,
    private val getFilteredTasksUseCase: GetFilteredTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getTaskEndPomodoroSessionUseCase().collect { taskAnPomodoroSessions ->
                _uiState.update { state ->
                    state.copy(
                        taskCurrent = taskAnPomodoroSessions?.task,
                    )
                }

            }
        }
        observePomodoro()
        startTicker()
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            getFilteredTasksUseCase().collect { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }
        }
    }

    private fun observePomodoro() {
        viewModelScope.launch {
            repository.pomodoroFlow.collectLatest { state ->
                _uiState.update {
                    it.copy(
                        timeSession = (state.duration / 1000).toInt(),
                        isPlay = state.isRunning,
                        statusSession = state.statusSession,
                        pomodoroStatus = state.pomodoroStatus,
                        currentSession = state.counterPomodoro + 1
                    )
                }
            }
        }
    }

    private fun startTicker() {
        viewModelScope.launch {
            while (true) {
                delay(1000)

                val state = repository.pomodoroFlow.first()
                val remaining = repository.getRemaining(state) / 1000

                _uiState.update {
                    it.copy(time = remaining.toInt())
                }
            }
        }
    }

    private fun resetTaskCurrentPomodoro() {
        viewModelScope.launch {
            setCurrentTaskPomodoroUseCase(id = null)
        }
    }

    fun onEvent(event: PomodoroEvent) {
        when (event) {
            is PomodoroEvent.OnShowPomodoroSettingBottomSheet -> {
                _uiState.update {
                    it.copy(showPomodoroSettingBottomSheet = event.show)
                }
            }

            PomodoroEvent.OnResetTaskCurrentPomodoro -> resetTaskCurrentPomodoro()

            is PomodoroEvent.OnShowSelectTaskBottomSheet -> {
                _uiState.update {
                    it.copy(showSelectTaskBottomSheet = event.show)
                }
            }

            is PomodoroEvent.OnSelectTask -> {
                viewModelScope.launch {
                    setCurrentTaskPomodoroUseCase(id = event.id)
                    _uiState.update { it.copy(showSelectTaskBottomSheet = false) }
                }
            }
        }
    }
}
