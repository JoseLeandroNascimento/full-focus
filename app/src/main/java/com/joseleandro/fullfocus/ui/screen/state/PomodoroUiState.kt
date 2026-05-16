package com.joseleandro.fullfocus.ui.screen.state

import com.joseleandro.fullfocus.data.local.database.model.PomodoroState

data class PomodoroUiState(
    val isRunning: Boolean = false,
    val progressPercent: Float = 0f,
    val durationTime: Long = 0L,
    val statePomodoro: PomodoroState = PomodoroState.FOCUS,
    val progressPomodoro: Boolean = false
)
