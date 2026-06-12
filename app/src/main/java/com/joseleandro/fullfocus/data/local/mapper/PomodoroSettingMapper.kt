package com.joseleandro.fullfocus.data.local.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import com.joseleandro.fullfocus.domain.model.PomodoroSettingDomain

fun PomodoroSetting.toDomain(): PomodoroSettingDomain =
    PomodoroSettingDomain(
        focusTime = this.focusTime,
        longPauseTime = this.longPauseTime,
        shortPauseTime = this.shortPauseTime,
        sessionsUntilLongPause = this.sessionsUntilLongPause,
        focusProgressColor = Color.fromColorLong(this.focusProgressColor),
        longBreakProgressColor = Color.fromColorLong(this.longBreakProgressColor),
        shortBreakProgressColor = Color.fromColorLong(this.shortBreakProgressColor),
        soundFocus = this.soundFocus,
        soundPause = this.soundPause,
        volumeFocus = this.volumeFocus,
        volumePause = this.volumePause,
        isSoundEnabled = this.isSoundEnabled
    )