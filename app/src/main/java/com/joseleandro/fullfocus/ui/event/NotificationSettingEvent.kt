package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.data.local.preferences.model.SoundAlarm
import com.joseleandro.fullfocus.ui.state.NotificationSettingModalUiState

sealed interface NotificationSettingEvent {
    object LoadData : NotificationSettingEvent
    data class UpdateAlertSoundFocus(val sound: SoundAlarm?) : NotificationSettingEvent
    data class UpdateAlertSoundPause(val sound: SoundAlarm?) : NotificationSettingEvent
    data class UpdateVibrationEnabled(val isEnabled: Boolean) : NotificationSettingEvent
    data class ShowModal(val modal: NotificationSettingModalUiState) : NotificationSettingEvent
    data object CloseModal : NotificationSettingEvent
}