package com.joseleandro.fullfocus.ui.screen.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.core.util.VibrationHelper
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.domain.effect.PomodoroEffect
import com.joseleandro.fullfocus.domain.model.PomodoroDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.state.PomodoroModalUiState
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val pomodoroRepository: PomodoroRepository,
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val vibrationHelper: VibrationHelper
) : ViewModel() {

    private val _modal = MutableStateFlow<PomodoroModalUiState>(PomodoroModalUiState.None)


    val uiState: StateFlow<PomodoroUiState> = combine(
        pomodoroRepository.pomodoro,
        pomodoroSettingRepository.pomodoroSetting,
        _modal
    ) { pomodoro, pomodoroSetting, modal ->
        PomodoroUiState(
            isRunning = pomodoro.isRunning,
            duration = pomodoro.duration,
            pomodoroState = pomodoro.pomodoroState,
            progressPercent = pomodoro.calcPercentTime(),
            focusCount = pomodoro.focusCount,
            colorProgress = when (pomodoro.pomodoroState) {
                PomodoroState.FOCUS -> pomodoroSetting.focusProgressColor
                PomodoroState.SHORT_PAUSE -> pomodoroSetting.shortBreakProgressColor
                PomodoroState.LONG_PAUSE -> pomodoroSetting.longBreakProgressColor
            },
            completedPomodoroCount = pomodoro.completedPomodoroCount,
            sessionsUntilLongPause = pomodoro.sessionsUntilLongPause,
            modal = modal
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PomodoroUiState()
    )

    init {
        observeSessionCompletion()
        observePomodoroEffect()
    }

    private fun observeSessionCompletion() {
        viewModelScope.launch {

            pomodoroRepository.pomodoro
                .filter { it.isRunning && it.duration > 0 && it.time >= it.duration }
                .collect {
                    onEvent(PomodoroEvent.CompleteSession)
                }
        }
    }

    private fun observePomodoroEffect() {

        viewModelScope.launch {
            pomodoroRepository.effect
                .collect { pomodoroEffect ->
                    vibrationHelper.vibrate()
                    when (pomodoroEffect) {
                        PomodoroEffect.FocusFinished -> _modal.value =
                            PomodoroModalUiState.FocusFinished

                        PomodoroEffect.LongBreakFinished -> _modal.value =
                            PomodoroModalUiState.LongBreakFinished

                        PomodoroEffect.ShortBreakFinished -> _modal.value =
                            PomodoroModalUiState.ShortBreakFinished
                    }
                }
        }
    }

    fun onEvent(event: PomodoroEvent) {
        when (event) {
            PomodoroEvent.CloseModal -> _modal.value = PomodoroModalUiState.None
            is PomodoroEvent.ShowModal -> _modal.value = event.modal
            PomodoroEvent.CancelAndSave -> viewModelScope.launch { pomodoroRepository.cancelAndSave() }
            PomodoroEvent.CancelAndDelete -> viewModelScope.launch { pomodoroRepository.cancelAndDelete() }
            PomodoroEvent.Pause -> viewModelScope.launch { pomodoroRepository.pause() }
            PomodoroEvent.Play -> viewModelScope.launch { pomodoroRepository.play() }
            PomodoroEvent.Reverse -> viewModelScope.launch { pomodoroRepository.reverse() }
            PomodoroEvent.Skip -> viewModelScope.launch { pomodoroRepository.skip() }
            PomodoroEvent.CompleteSession -> viewModelScope.launch { pomodoroRepository.completeSession() }
        }
    }

    private fun PomodoroDomain.calcPercentTime(): Float {
        if (duration == 0L) return 1f
        val remaining = (duration - time).coerceAtLeast(0)
        return remaining.toFloat() / duration
    }
}
