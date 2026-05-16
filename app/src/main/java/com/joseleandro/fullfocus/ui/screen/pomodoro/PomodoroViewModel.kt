package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.model.PomodoroTimerDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroTimerRepository
import com.joseleandro.fullfocus.ui.screen.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.screen.state.PomodoroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val pomodoroTimerRepository: PomodoroTimerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            pomodoroTimerRepository.pomodoroTimer.collect { pomodoroTimerDomain ->
                _uiState.update { state ->
                    state.copy(
                        isRunning = pomodoroTimerDomain.isRunning,
                        durationTime = pomodoroTimerDomain.duration,
                        progressPercent = pomodoroTimerDomain.calcPercentTime(),
                        progressPomodoro = pomodoroTimerDomain.progress
                    )
                }
            }
        }
    }

    fun onEvent(event: PomodoroEvent) {
        when (event) {
            PomodoroEvent.OnPlay -> play()
            PomodoroEvent.OnPause -> pause()
            PomodoroEvent.OnCancel -> cancel()
            PomodoroEvent.OnRestart -> restart()
        }
    }

    private fun play() {
        viewModelScope.launch {
            pomodoroTimerRepository.play()
        }
    }

    private fun pause() {
        viewModelScope.launch {
            pomodoroTimerRepository.pause()
        }
    }

    private fun cancel(){
        viewModelScope.launch {
            pomodoroTimerRepository.cancel()
        }
    }

    private fun restart(){
        viewModelScope.launch {
            pomodoroTimerRepository.restart()
        }
    }

    private fun PomodoroTimerDomain.calcPercentTime(): Float {
        if (this.duration == 0L) return 0f
        return (this.time.toFloat() / this.duration)
    }

    companion object {
        const val TAG = "PomodoroViewModel"
    }
}
