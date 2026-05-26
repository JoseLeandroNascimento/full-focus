package com.joseleandro.fullfocus.ui.state

import androidx.compose.ui.graphics.Color
import com.joseleandro.fullfocus.ui.screen.pomodoro_setting.component.PickerColorType

sealed interface PomodoroSettingModalUiState {

    data object None : PomodoroSettingModalUiState

    data object FocusTimer : PomodoroSettingModalUiState

    data object ShortBreakTimer : PomodoroSettingModalUiState

    data object LongBreakTimer : PomodoroSettingModalUiState

    data class PickerColor(val color: Color, val type: PickerColorType) :
        PomodoroSettingModalUiState

}