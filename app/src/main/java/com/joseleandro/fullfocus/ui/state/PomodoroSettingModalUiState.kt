package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.ui.theme.ColorStyle

sealed interface PomodoroSettingModalUiState {

    data object None : PomodoroSettingModalUiState

    data object FocusTimer : PomodoroSettingModalUiState

    data object ShortBreakTimer : PomodoroSettingModalUiState

    data object LongBreakTimer : PomodoroSettingModalUiState

}