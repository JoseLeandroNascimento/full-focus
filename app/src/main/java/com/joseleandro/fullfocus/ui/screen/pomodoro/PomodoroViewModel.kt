package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.model.PomodoroDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.state.PomodoroModalUiState
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            pomodoroRepository.pomodoro.collect { pomodoroDomain ->
                _uiState.update { state ->
                    state.copy(
                        isRunning = pomodoroDomain.isRunning,
                        duration = pomodoroDomain.duration,
                        pomodoroState = pomodoroDomain.pomodoroState,
                        progressPercent = pomodoroDomain.calcPercentTime(),
                        focusCount = pomodoroDomain.focusCount,
                        sessionsUntilLongPause = pomodoroDomain.sessionsUntilLongPause
                    )
                }

                if (pomodoroDomain.isRunning &&
                    pomodoroDomain.duration > 0 &&
                    pomodoroDomain.time >= pomodoroDomain.duration
                ) {
                    onEvent(PomodoroEvent.CompleteSession)
                }
            }
        }
    }

    fun onEvent(event: PomodoroEvent) {
        when (event) {
            PomodoroEvent.CloseModal -> closeModal()
            is PomodoroEvent.ShowModal -> showModal(modal = event.modal)
            PomodoroEvent.CancelAndSave -> viewModelScope.launch { pomodoroRepository.cancelAndSave() }
            PomodoroEvent.CancelAndDelete -> viewModelScope.launch { pomodoroRepository.cancelAndDelete() }
            PomodoroEvent.Pause -> viewModelScope.launch { pomodoroRepository.pause() }
            PomodoroEvent.Play -> viewModelScope.launch { pomodoroRepository.play() }
            PomodoroEvent.Reverse -> viewModelScope.launch { pomodoroRepository.reverse() }
            PomodoroEvent.Skip -> viewModelScope.launch { pomodoroRepository.skip() }
            PomodoroEvent.CompleteSession -> viewModelScope.launch { pomodoroRepository.completeSession() }
        }
    }

    private fun closeModal() {
        _uiState.update { state ->
            state.copy(
                modal = PomodoroModalUiState.None
            )
        }
    }

    private fun showModal(modal: PomodoroModalUiState) {
        _uiState.update { state ->
            state.copy(
                modal = modal
            )
        }
    }

    private fun PomodoroDomain.calcPercentTime(): Float {
        if (this.duration == 0L) return 1f
        val remaining = (this.duration - this.time).coerceAtLeast(0)
        return (remaining.toFloat() / this.duration)
    }
}
