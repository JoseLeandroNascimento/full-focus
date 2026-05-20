package com.joseleandro.fullfocus.ui.state

sealed interface PomodoroSettingModalUiState {

    data object None : PomodoroSettingModalUiState

    data object FocusTimer : PomodoroSettingModalUiState

    data object ShortBreakTimer : PomodoroSettingModalUiState

    data object LongBreakTimer : PomodoroSettingModalUiState

}