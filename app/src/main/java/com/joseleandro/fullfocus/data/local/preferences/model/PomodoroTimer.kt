package com.joseleandro.fullfocus.data.local.preferences.model

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroTimer(
    val startTime: Long = 0,
    val endTime: Long? = null,
    val isRunning: Boolean = false,
    val duration: Long = 25 * 60 * 1_000
)
