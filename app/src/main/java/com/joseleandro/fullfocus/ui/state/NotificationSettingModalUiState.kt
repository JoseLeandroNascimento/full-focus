package com.joseleandro.fullfocus.ui.state

sealed interface NotificationSettingModalUiState {

    object None : NotificationSettingModalUiState

    object AlertSoundFocus : NotificationSettingModalUiState

    object AlertSoundPause : NotificationSettingModalUiState

}