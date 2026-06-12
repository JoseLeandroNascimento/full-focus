package com.joseleandro.fullfocus.domain.model

import androidx.compose.ui.graphics.Color
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground

data class PomodoroSettingDomain(
    val shortPauseTime: Long = 0,
    val longPauseTime: Long = 0,
    val focusTime: Long = 0,
    val sessionsUntilLongPause: Int = 0,
    val focusProgressColor: Color = Color.Unspecified,
    val shortBreakProgressColor: Color = Color.Unspecified,
    val longBreakProgressColor: Color = Color.Unspecified,
    val volumeFocus: Int = 0,
    val volumePause: Int = 0,
    val soundFocus: SoundBackground? = null,
    val soundPause: SoundBackground? = null,
    val isSoundEnabled: Boolean = true
)
