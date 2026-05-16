package com.joseleandro.fullfocus.data.local.preferences.model

import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import kotlinx.serialization.Serializable

@Serializable
data class PomodoroTimer(
    val startTime: Long = 0,
    val endTime: Long? = null,
    val isRunning: Boolean = false,
    val duration: Long = 0,
    val pomodoroState: PomodoroState = PomodoroState.FOCUS,
    val progress: Boolean = false
)


