package com.joseleandro.fullfocus.data.local.preferences.data

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroTimePreferences(
    val startTime: Long = 0L,
    val duration: Long = 25 * 60 * 1000L,
    val pausedAt: Long? = null,
    val isRunning: Boolean = false,
    val statusSession: StatusSession = StatusSession.FOCUS,
    val counterPomodoro: Int = 0,
    val pomodoroIntervalBreakLong: Int = 4
) {

    val pomodoroStatus: PomodoroStatus
        get() = when {
            startTime == 0L -> PomodoroStatus.START
            startTime > 0L -> PomodoroStatus.PROGRESS
            else -> PomodoroStatus.FINISHED
        }
}
