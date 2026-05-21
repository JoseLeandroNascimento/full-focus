package com.joseleandro.fullfocus.ui.state

data class PomodoroSettingUiState(
    val changedSetting: Boolean = false,
    val focusTime: String = "",
    val shortBreakTime: String = "",
    val longBreakTime: String = "",
    val modal: PomodoroSettingModalUiState = PomodoroSettingModalUiState.None
)
