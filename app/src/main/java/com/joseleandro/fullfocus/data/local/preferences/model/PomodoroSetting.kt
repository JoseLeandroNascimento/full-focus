package com.joseleandro.fullfocus.data.local.preferences.model

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroSetting(
    val shortPauseTime: Long = 5 * 60 * 1_000,
    val longPauseTime: Long = 15 * 60 * 1_000,
    val focusTime: Long = 25 * 60 * 1_000
)
