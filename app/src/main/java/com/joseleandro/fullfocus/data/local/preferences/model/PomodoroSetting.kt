package com.joseleandro.fullfocus.data.local.preferences.model

import com.joseleandro.fullfocus.ui.theme.ColorStyle
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
    val focusProgressColor: ColorStyle = ColorStyle.fromColor(Primary),
    val shortBreakProgressColor: ColorStyle = ColorStyle.fromColor(ShortPause),
    val longBreakProgressColor: ColorStyle = ColorStyle.fromColor(LongPause),
    val volumeFocus: Int = 50,
    val volumePause: Int = 50,
    val soundFocus: SoundBackground = SoundBackground.RAIN,
    val soundPause: SoundBackground = SoundBackground.FOREST,
    val isSoundEnabled: Boolean = true,
    val alertSoundFocus: SoundAlarm? = SoundAlarm.ALARM_1,
    val alertSoundPause: SoundAlarm? = SoundAlarm.ALARM_1,
    val isVibrationEnabled: Boolean = true,
    val dailyGoal: Int = 4,
    val weeklyGoal: Int = 5
)
