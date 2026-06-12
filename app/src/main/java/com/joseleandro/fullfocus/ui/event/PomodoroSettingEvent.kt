package com.joseleandro.fullfocus.ui.event

import androidx.compose.ui.graphics.Color
import com.joseleandro.fullfocus.ui.state.PomodoroSettingModalUiState

sealed interface PomodoroSettingEvent {

    data object LoadData : PomodoroSettingEvent

    data class UpdateFocusTime(val time: String) : PomodoroSettingEvent

    data class UpdateShortBreakTime(val time: String) : PomodoroSettingEvent

    data class UpdateLongBreakTime(val time: String) : PomodoroSettingEvent

    data class UpdateFocusProgressColor(val color: Color) : PomodoroSettingEvent

    data class UpdateShortBreakProgressColor(val color: Color) : PomodoroSettingEvent

    data class UpdateLongBreakProgressColor(val color: Color) : PomodoroSettingEvent

    data class UpdateSilentMode(val value: Boolean) : PomodoroSettingEvent

    data class ShowModal(val modal: PomodoroSettingModalUiState) : PomodoroSettingEvent

    data object CloseModal : PomodoroSettingEvent

    data class ChangedSetting(val value: Boolean) : PomodoroSettingEvent

    data object OnSave : PomodoroSettingEvent
}