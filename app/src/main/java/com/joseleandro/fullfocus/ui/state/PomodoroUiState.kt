package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.database.model.PomodoroState

data class PomodoroUiState(
    val duration: Long = 0,
    val progressPercent: Float = 0f,
    val isRunning: Boolean = false,
    val pomodoroState: PomodoroState = PomodoroState.FOCUS,
    val focusCount: Int = 0,
    val completedPomodoroCount: Int = 0,
    val sessionsUntilLongPause: Int = 4,
    val modal: PomodoroModalUiState = PomodoroModalUiState.None
)
