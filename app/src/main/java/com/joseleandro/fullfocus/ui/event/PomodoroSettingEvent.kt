package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.state.PomodoroSettingModalUiState

sealed interface PomodoroSettingEvent {

    data class UpdateFocusTime(val time: String) : PomodoroSettingEvent

    data class UpdateShortBreakTime(val time: String) : PomodoroSettingEvent

    data class UpdateLongBreakTime(val time: String) : PomodoroSettingEvent

    data class ShowModal(val modal: PomodoroSettingModalUiState) : PomodoroSettingEvent

    data object CloseModal : PomodoroSettingEvent

    data object OnSave: PomodoroSettingEvent
}