package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.enums.SessionStatus
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()
    private var timerJob: Job? = null

    fun onEvent(event: PomodoroEvent) {

        when (event) {
            PomodoroEvent.OnPlay -> play()
            PomodoroEvent.OnPause -> pause()
            PomodoroEvent.OnRestart -> restart()
            is PomodoroEvent.OnShowPomodoroSettingBottomSheet -> changeVisibilityPomodoroBottomSheet(
                event.show
            )
        }
    }

    private fun startTimer() {

        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (true) {

                val currentState = _uiState.value

                if (!currentState.isPlay) break

                if (currentState.time <= 0) {
                    onTimerFinished()
                    break
                }

                delay(1000)

                _uiState.update { state ->
                    state.copy(time = state.time - 1)
                }
            }
        }
    }

    private fun play() {
        _uiState.update { uiState ->
            uiState.copy(
                isPlay = true,
                sessionStatus = SessionStatus.PROGRESS
            )
        }
        startTimer()
    }

    private fun pause() {
        _uiState.update { uiState ->
            uiState.copy(
                isPlay = false
            )
        }
        timerJob?.cancel()
    }

    private fun restart() {
        timerJob?.cancel()
        _uiState.update { uiState ->
            uiState.copy(
                isPlay = false,
                time = PomodoroUiState().time,
                sessionStatus = SessionStatus.START
            )
        }
    }

    private fun changeVisibilityPomodoroBottomSheet(show: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(
                showPomodoroSettingBottomSheet = show
            )
        }
    }

    private fun onTimerFinished() {
        _uiState.update {
            it.copy(
                isPlay = false,
                sessionStatus = SessionStatus.FINISHED
            )
        }

        // 🔔 aqui você pode:
        // - tocar som
        // - vibrar
        // - mudar para modo descanso
    }

}