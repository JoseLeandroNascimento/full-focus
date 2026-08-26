package com.joseleandro.fullfocus.data.local.mapper

import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import com.joseleandro.fullfocus.domain.model.PomodoroSettingDomain

fun PomodoroSetting.toDomain(): PomodoroSettingDomain =
    PomodoroSettingDomain(
        focusTime = this.focusTime,
        longPauseTime = this.longPauseTime,
        shortPauseTime = this.shortPauseTime,
        sessionsUntilLongPause = this.sessionsUntilLongPause,
        focusProgressColor = this.focusProgressColor,
        longBreakProgressColor = this.longBreakProgressColor,
        shortBreakProgressColor = this.shortBreakProgressColor,
        soundFocus = this.soundFocus,
        soundPause = this.soundPause,
        volumeFocus = this.volumeFocus,
        volumePause = this.volumePause,
        isSoundEnabled = this.isSoundEnabled,
        alertSoundFocus = this.alertSoundFocus,
        alertSoundPause = this.alertSoundPause,
        isVibrationEnabled = this.isVibrationEnabled,
        dailyGoal = this.dailyGoal,
        weeklyGoal = this.weeklyGoal
    )