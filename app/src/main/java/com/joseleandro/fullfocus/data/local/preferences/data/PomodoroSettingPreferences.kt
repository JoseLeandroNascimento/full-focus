package com.joseleandro.fullfocus.data.local.preferences.data

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroSettingPreferences(
    val pomodoroDuration: Long = 25 * 60 * 1000L,
    val shortBreakDuration: Long = 5 * 60 * 1000L,
    val longBreakDuration: Long = 15 * 60 * 1000L,
    val longBreakInterval: Int = 4,
    val autoStartNextPomodoro: Boolean = false,
    val autoStartNextShortBreak: Boolean = false,
    val autoStartNextLongBreak: Boolean = false
)
