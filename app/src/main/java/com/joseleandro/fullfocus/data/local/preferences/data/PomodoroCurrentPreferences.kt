package com.joseleandro.fullfocus.data.local.preferences.data

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroCurrentPreferences(
    val startTime: Long = 0L,
    val duration: Long = 25 * 60 * 1000L,
    val pausedAt: Long? = null,
    val isRunning: Boolean = false
)