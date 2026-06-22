package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.preferences.model.SoundAlarm

data class NotificationSettingUiState(
    val alertSoundFocus: SoundAlarm? = null,
    val alertSoundPause: SoundAlarm? = null,
    val isVibrationEnabled: Boolean = false,
    val modal: NotificationSettingModalUiState = NotificationSettingModalUiState.None
)
