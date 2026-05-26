package com.joseleandro.fullfocus.data.local.preferences.model

import androidx.compose.ui.graphics.toColorLong
import com.joseleandro.fullfocus.ui.theme.LongPause
import com.joseleandro.fullfocus.ui.theme.Primary
import com.joseleandro.fullfocus.ui.theme.ShortPause
import kotlinx.serialization.Serializable

@Serializable
data class PomodoroSetting(
    val shortPauseTime: Long = 5 * 60 * 1_000,
    val longPauseTime: Long = 15 * 60 * 1_000,
    val focusTime: Long = 25 * 60 * 1_000,
    val sessionsUntilLongPause: Int = 4,
    val focusProgressColor: Long = Primary.toColorLong(),
    val shortBreakProgressColor: Long = ShortPause.toColorLong(),
    val longBreakProgressColor: Long = LongPause.toColorLong()
)
