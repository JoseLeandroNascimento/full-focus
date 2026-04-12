package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.enums.SessionStatus
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val repository: PomodoroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    init {
        observePomodoro()
        startTicker()
    }

    private fun observePomodoro() {
        viewModelScope.launch {
            repository.pomodoroFlow.collect { state ->
                _uiState.update {
                    it.copy(
                        timeSession = (state.duration / 1000).toInt(),
                        isPlay = state.isRunning,
                        sessionStatus = when {
                            state.isRunning -> SessionStatus.PROGRESS
                            state.startTime == 0L -> SessionStatus.START
                            else -> SessionStatus.FINISHED
                        }
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

    fun onEvent(event: PomodoroEvent) {
        when (event) {
            PomodoroEvent.OnPlay -> {} // agora é o service que controla
            PomodoroEvent.OnPause -> {}
            PomodoroEvent.OnRestart -> {}
            is PomodoroEvent.OnShowPomodoroSettingBottomSheet -> {
                _uiState.update {
                    it.copy(showPomodoroSettingBottomSheet = event.show)
                }
            }
        }
    }
}