package com.joseleandro.fullfocus.ui.state

sealed interface PomodoroModalUiState {

    data object None: PomodoroModalUiState

    data object PomodoroSettingModal : PomodoroModalUiState

}