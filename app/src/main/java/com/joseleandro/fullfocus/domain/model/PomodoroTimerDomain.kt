package com.joseleandro.fullfocus.domain.model

import com.joseleandro.fullfocus.data.local.database.model.PomodoroState

data class PomodoroTimerDomain(
    val time: Long = 0L,
    val isRunning: Boolean = false,
    val duration: Long = 0L,
    val pomodoroState: PomodoroState = PomodoroState.FOCUS,
    val progress: Boolean = false
)
