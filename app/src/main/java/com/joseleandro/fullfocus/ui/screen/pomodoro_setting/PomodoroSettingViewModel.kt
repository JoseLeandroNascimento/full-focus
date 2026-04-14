package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import com.joseleandro.fullfocus.domain.usecase.GetPomodoroSettingPreferencesUseCase
import com.joseleandro.fullfocus.domain.usecase.UpdatePomodoroSettingUseCase
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent
import com.joseleandro.fullfocus.ui.state.PomodoroSettingUiState
import com.joseleandro.fullfocus.ui.state.TimerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PomodoroSettingViewModel(
    private val getPomodoroSettingUseCase: GetPomodoroSettingPreferencesUseCase,
    private val updatePomodoroSettingUseCase: UpdatePomodoroSettingUseCase
) : ViewModel() {

    private val _modalState = MutableStateFlow(ModalState())

    val uiState: StateFlow<PomodoroSettingUiState> = combine(
        getPomodoroSettingUseCase(),
        _modalState
    ) { prefs, modal ->
        PomodoroSettingUiState(
            pomodoroTime = TimeUnit.MILLISECONDS.toMinutes(prefs.pomodoroDuration).toInt(),
            shortBreakTime = TimeUnit.MILLISECONDS.toMinutes(prefs.shortBreakDuration).toInt(),
            longBreakTime = TimeUnit.MILLISECONDS.toMinutes(prefs.longBreakDuration).toInt(),
            longBreakInterval = prefs.longBreakInterval,
            autoStartNextPomodoro = prefs.autoStartNextPomodoro,
            autoStartNextShortBreak = prefs.autoStartNextShortBreak,
            autoStartNextLongBreak = prefs.autoStartNextLongBreak,

            showTimePicker = modal.showTimePicker,
            selectedTimerType = modal.selectedTimerType,
            tempSelectedTime = modal.tempSelectedTime
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PomodoroSettingUiState()
    )

    fun onEvent(event: PomodoroSettingEvent) {
        when (event) {
            is PomodoroSettingEvent.OnTimerClick -> {
                val initialValue = when (event.type) {
                    TimerType.POMODORO -> uiState.value.pomodoroTime
                    TimerType.SHORT_BREAK -> uiState.value.shortBreakTime
                    TimerType.LONG_BREAK -> uiState.value.longBreakTime
                    TimerType.LONG_BREAK_INTERVAL -> uiState.value.longBreakInterval
                }
                _modalState.value = ModalState(
                    showTimePicker = true,
                    selectedTimerType = event.type,
                    tempSelectedTime = initialValue
                )
            }

            PomodoroSettingEvent.OnDismissTimePicker -> {
                _modalState.value = ModalState()
            }

            is PomodoroSettingEvent.OnTimerConfirm -> {
                saveTimerValue(uiState.value.selectedTimerType, event.value)
                _modalState.value = ModalState()
            }

            is PomodoroSettingEvent.OnAutoStartLongBreakChange -> {
                updatePreference { it.copy(autoStartNextLongBreak = event.value) }
            }

            is PomodoroSettingEvent.OnAutoStartPomodoroChange -> {
                updatePreference { it.copy(autoStartNextPomodoro = event.value) }
            }

            is PomodoroSettingEvent.OnAutoStartShortBreakChange -> {
                updatePreference { it.copy(autoStartNextShortBreak = event.value) }
            }

            is PomodoroSettingEvent.OnLongBreakIntervalChange -> {
                updatePreference { it.copy(longBreakInterval = event.value) }
            }
        }
    }

    private fun saveTimerValue(type: TimerType?, value: Int) {
        if (type == TimerType.LONG_BREAK_INTERVAL) {
            updatePreference { it.copy(longBreakInterval = value) }
            return
        }
        val durationMillis = TimeUnit.MINUTES.toMillis(value.toLong())
        updatePreference { current ->
            when (type) {
                TimerType.POMODORO -> current.copy(pomodoroDuration = durationMillis)
                TimerType.SHORT_BREAK -> current.copy(shortBreakDuration = durationMillis)
                TimerType.LONG_BREAK -> current.copy(longBreakDuration = durationMillis)
                else -> current
            }
        }
    }

    private fun updatePreference(update: (PomodoroSettingPreferences) -> PomodoroSettingPreferences) {
        viewModelScope.launch {
            val currentPrefs = getPomodoroSettingUseCase().first()
            val newPrefs = update(currentPrefs)
            updatePomodoroSettingUseCase(newPrefs)
        }
    }

    private data class ModalState(
        val showTimePicker: Boolean = false,
        val selectedTimerType: TimerType? = null,
        val tempSelectedTime: Int = 0
    )
}