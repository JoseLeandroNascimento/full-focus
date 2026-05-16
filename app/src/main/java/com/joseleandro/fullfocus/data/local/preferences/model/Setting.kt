package com.joseleandro.fullfocus.data.local.preferences.model

import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val pomodoroTimer: PomodoroTimer = PomodoroTimer()
)
