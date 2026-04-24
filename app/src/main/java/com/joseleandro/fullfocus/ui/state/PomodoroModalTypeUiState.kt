package com.joseleandro.fullfocus.ui.state

sealed interface PomodoroModalTypeUiState {
    data object None : PomodoroModalTypeUiState
    data object SelectTask : PomodoroModalTypeUiState
    data object Settings : PomodoroModalTypeUiState
    data object ConfirmCancel : PomodoroModalTypeUiState
}
