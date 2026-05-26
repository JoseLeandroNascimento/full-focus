package com.joseleandro.fullfocus.ui.screen.pomodoro_setting

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.ui.effect.PomodoroSettingEffect
import com.joseleandro.fullfocus.ui.event.PomodoroSettingEvent
import com.joseleandro.fullfocus.ui.state.PomodoroSettingModalUiState
import com.joseleandro.fullfocus.ui.state.PomodoroSettingUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class PomodoroSettingViewModel(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroSettingUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PomodoroSettingEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: PomodoroSettingEvent) {

        when (event) {
            PomodoroSettingEvent.CloseModal -> closeModal()
            is PomodoroSettingEvent.ShowModal -> showModal(modal = event.modal)
            is PomodoroSettingEvent.UpdateFocusTime -> updateFocusTime(time = event.time)
            is PomodoroSettingEvent.UpdateLongBreakTime -> updateLongBreakTime(time = event.time)
            is PomodoroSettingEvent.UpdateShortBreakTime -> updateShortBreakTime(time = event.time)
            PomodoroSettingEvent.OnSave -> save()
            is PomodoroSettingEvent.ChangedSetting -> changedSetting(value = event.value)
            PomodoroSettingEvent.LoadData -> loadData()
            is PomodoroSettingEvent.UpdateFocusProgressColor -> updateFocusProgressColor(color = event.color)
            is PomodoroSettingEvent.UpdateLongBreakProgressColor -> updateLongBreakProgressColor(
                color = event.color
            )

            is PomodoroSettingEvent.UpdateShortBreakProgressColor -> updateShortBreakProgressColor(
                color = event.color
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            pomodoroSettingRepository.pomodoroSetting.collect { pomodoroSetting ->
                _uiState.update { state ->
                    state.copy(
                        changedSetting = false,
                        focusTime = pomodoroSetting.focusTime.formattedTime(),
                        longBreakTime = pomodoroSetting.longPauseTime.formattedTime(),
                        shortBreakTime = pomodoroSetting.shortPauseTime.formattedTime(),
                        focusProgressColor = pomodoroSetting.focusProgressColor,
                        longBreakProgressColor = pomodoroSetting.longBreakProgressColor,
                        shortBreakProgressColor = pomodoroSetting.shortBreakProgressColor
                    )
                }
            }
        }
    }

    private fun updateFocusProgressColor(color: Color) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    focusProgressColor = color,
                    changedSetting = true
                )
            }
            closeModal()
        }
    }

    private fun updateShortBreakProgressColor(color: Color) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    shortBreakProgressColor = color,
                    changedSetting = true
                )
            }
            closeModal()
        }
    }

    private fun updateLongBreakProgressColor(color: Color) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    longBreakProgressColor = color,
                    changedSetting = true
                )
            }
            closeModal()
        }
    }

    private fun updateFocusTime(time: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    focusTime = time.parseTimeToMillis().formattedTime(),
                    changedSetting = true
                )
            }
            closeModal()
        }
    }

    private fun updateShortBreakTime(time: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    shortBreakTime = time.parseTimeToMillis().formattedTime(),
                    changedSetting = true
                )
            }
            closeModal()
        }
    }

    private fun updateLongBreakTime(time: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    longBreakTime = time.parseTimeToMillis().formattedTime(),
                    changedSetting = true
                )
            }
            closeModal()
        }
    }

    private fun showModal(modal: PomodoroSettingModalUiState) {
        _uiState.update { state ->
            state.copy(
                modal = modal
            )
        }
    }

    private fun changedSetting(value: Boolean) =
        _uiState.update { state ->
            state.copy(
                changedSetting = value
            )
        }

    private fun save() {

        viewModelScope.launch {
            with(_uiState.value) {
                pomodoroSettingRepository.updateFocusTime(focusTime.parseTimeToMillis())
                pomodoroSettingRepository.updateShortBreakTime(shortBreakTime.parseTimeToMillis())
                pomodoroSettingRepository.updateLongBreakTime(longBreakTime.parseTimeToMillis())
                pomodoroSettingRepository.updateFocusProgressColor(color = focusProgressColor)
                pomodoroSettingRepository.updateLongBreakProgressColor(color = longBreakProgressColor)
                pomodoroSettingRepository.updateShortBreakProgressColor(color = shortBreakProgressColor)
            }

            changedSetting(value = false)

            _effect.emit(PomodoroSettingEffect.CloseBottomSheet)
        }

    }

    private fun closeModal() {
        _uiState.update { state ->
            state.copy(
                modal = PomodoroSettingModalUiState.None
            )
        }
    }

    private fun String.parseTimeToMillis(): Long {
        val minutes = this.split(":")[0].toLongOrNull() ?: 0L
        return minutes * 60 * 1000
    }

    private fun Long.formattedTime(): String {
        val minute = this / 60_000
        val second = (this % 60_000) / 1000
        return String.format(Locale.getDefault(), "%02d:%02d", minute, second)
    }
}