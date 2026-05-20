package com.joseleandro.fullfocus.domain.model

import com.joseleandro.fullfocus.data.local.database.model.PomodoroState

data class PomodoroDomain(
    val time: Long = 0,
    val duration: Long = 0,
    val pomodoroState: PomodoroState = PomodoroState.FOCUS,
    val isRunning: Boolean = false,
    val focusCount: Int = 0,
    val sessionsUntilLongPause: Int = 4
)
