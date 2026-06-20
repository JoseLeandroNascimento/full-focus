package com.joseleandro.fullfocus.domain.model

import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.theme.ColorStyle

data class PomodoroSettingDomain(
    val shortPauseTime: Long = 0,
    val longPauseTime: Long = 0,
    val focusTime: Long = 0,
    val sessionsUntilLongPause: Int = 0,
    val focusProgressColor: ColorStyle = ColorStyle.Solid(0),
    val shortBreakProgressColor: ColorStyle = ColorStyle.Solid(0),
    val longBreakProgressColor: ColorStyle = ColorStyle.Solid(0),
    val volumeFocus: Int = 0,
    val volumePause: Int = 0,
    val soundFocus: SoundBackground? = null,
    val soundPause: SoundBackground? = null,
    val isSoundEnabled: Boolean = true
)
