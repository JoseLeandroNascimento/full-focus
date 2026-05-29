package com.joseleandro.fullfocus.ui.state

sealed interface PomodoroModalUiState {

    data object None : PomodoroModalUiState

    data object PomodoroSetting : PomodoroModalUiState

    data object CancelOptions : PomodoroModalUiState

    data object FocusFinished : PomodoroModalUiState

    data object ShortBreakFinished : PomodoroModalUiState

    data object LongBreakFinished : PomodoroModalUiState

}
