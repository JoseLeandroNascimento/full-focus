package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.ui.theme.ColorStyle

data class PomodoroUiState(
    val duration: Long = 0,
    val progressPercent: Float = 0f,
    val isRunning: Boolean = false,
    val pomodoroState: PomodoroState = PomodoroState.FOCUS,
    val focusCount: Int = 0,
    val completedPomodoroCount: Int = 0,
    val sessionsUntilLongPause: Int = 4,
    val colorProgress: ColorStyle = ColorStyle.Solid(0),
    val modal: PomodoroModalUiState = PomodoroModalUiState.None
)
