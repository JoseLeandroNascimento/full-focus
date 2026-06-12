package com.joseleandro.fullfocus.ui.state

import androidx.compose.ui.graphics.Color

data class PomodoroSettingUiState(
    val changedSetting: Boolean = false,
    val focusTime: String = "",
    val shortBreakTime: String = "",
    val longBreakTime: String = "",
    val focusProgressColor: Color = Color.Unspecified,
    val shortBreakProgressColor: Color = Color.Unspecified,
    val longBreakProgressColor: Color = Color.Unspecified,
    val silentMode: Boolean = false,
    val modal: PomodoroSettingModalUiState = PomodoroSettingModalUiState.None
)
