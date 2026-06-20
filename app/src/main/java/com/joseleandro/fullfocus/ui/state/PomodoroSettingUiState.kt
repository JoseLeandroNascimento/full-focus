package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.theme.ColorStyle

data class PomodoroSettingUiState(
    val changedSetting: Boolean = false,
    val focusTime: String = "",
    val shortBreakTime: String = "",
    val longBreakTime: String = "",
    val focusProgressColor: ColorStyle = ColorStyle.Solid(0),
    val shortBreakProgressColor: ColorStyle = ColorStyle.Solid(0),
    val longBreakProgressColor: ColorStyle = ColorStyle.Solid(0),
    val soundFocus: SoundBackground? = null,
    val soundPause: SoundBackground? = null,
    val silentMode: Boolean = false,
    val modal: PomodoroSettingModalUiState = PomodoroSettingModalUiState.None
)
