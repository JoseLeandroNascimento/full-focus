package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class PomodoroSettingViewModel(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroSettingUiState())
    val uiState: StateFlow<PomodoroSettingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            pomodoroSettingRepository.pomodoroSetting.collect { setting ->
                _uiState.update { state ->
                    state.copy(
                        focusTime = formatTime(setting.focusTime),
                        shortPauseTime = formatTime(setting.shortPauseTime),
                        longPauseTime = formatTime(setting.longPauseTime)
                    )
                }
            }
        }
    }

    fun onEvent(event: PomodoroSettingEvent) {
        when (event) {
            is PomodoroSettingEvent.OnFocusTimeChange -> {
                _uiState.update { it.copy(focusTime = event.time, showFocusPicker = false) }
            }
            is PomodoroSettingEvent.OnShortPauseTimeChange -> {
                _uiState.update { it.copy(shortPauseTime = event.time, showShortPausePicker = false) }
            }
            is PomodoroSettingEvent.OnLongPauseTimeChange -> {
                _uiState.update { it.copy(longPauseTime = event.time, showLongPausePicker = false) }
            }
            PomodoroSettingEvent.OnSave -> saveSettings()
            is PomodoroSettingEvent.OnShowPicker -> {
                _uiState.update { state ->
                    when (event.pickerType) {
                        PickerType.FOCUS -> state.copy(showFocusPicker = true)
                        PickerType.SHORT_PAUSE -> state.copy(showShortPausePicker = true)
                        PickerType.LONG_PAUSE -> state.copy(showLongPausePicker = true)
                    }
                }
            }
            PomodoroSettingEvent.OnDismissPicker -> {
                _uiState.update { it.copy(
                    showFocusPicker = false,
                    showShortPausePicker = false,
                    showLongPausePicker = false
                ) }
            }
        }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            val focusTimeMillis = parseTimeToMillis(_uiState.value.focusTime)
            val shortPauseTimeMillis = parseTimeToMillis(_uiState.value.shortPauseTime)
            val longPauseTimeMillis = parseTimeToMillis(_uiState.value.longPauseTime)

            pomodoroSettingRepository.updatePomodoroSetting(
                focusTime = focusTimeMillis,
                shortPauseTime = shortPauseTimeMillis,
                longPauseTime = longPauseTimeMillis
            )
        }
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        return String.format(Locale.getDefault(), "%02d:00", minutes)
    }

    private fun parseTimeToMillis(time: String): Long {
        val minutes = time.split(":")[0].toLongOrNull() ?: 0L
        return minutes * 60 * 1000
    }
}

data class PomodoroSettingUiState(
    val focusTime: String = "25:00",
    val shortPauseTime: String = "05:00",
    val longPauseTime: String = "15:00",
    val showFocusPicker: Boolean = false,
    val showShortPausePicker: Boolean = false,
    val showLongPausePicker: Boolean = false,
    val focusItems: List<String> = (1..60).map { String.format(Locale.getDefault(), "%02d:00", it) },
    val shortPauseItems: List<String> = (1..15).map { String.format(Locale.getDefault(), "%02d:00", it) },
    val longPauseItems: List<String> = (1..45).map { String.format(Locale.getDefault(), "%02d:00", it) }
)

sealed interface PomodoroSettingEvent {
    data class OnFocusTimeChange(val time: String) : PomodoroSettingEvent
    data class OnShortPauseTimeChange(val time: String) : PomodoroSettingEvent
    data class OnLongPauseTimeChange(val time: String) : PomodoroSettingEvent
    data class OnShowPicker(val pickerType: PickerType) : PomodoroSettingEvent
    data object OnDismissPicker : PomodoroSettingEvent
    data object OnSave : PomodoroSettingEvent
}

enum class PickerType {
    FOCUS, SHORT_PAUSE, LONG_PAUSE
}
