package com.joseleandro.fullfocus.ui.screen.notification_setting

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.core.util.BackgroundSoundPlayer
import com.joseleandro.fullfocus.data.local.preferences.model.SoundAlarm
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.ui.event.NotificationSettingEvent
import com.joseleandro.fullfocus.ui.state.NotificationSettingModalUiState
import com.joseleandro.fullfocus.ui.state.NotificationSettingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationSettingViewModel(
    private val backgroundSoundPlayer: BackgroundSoundPlayer,
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettingUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: NotificationSettingEvent) {

        when (event) {
            NotificationSettingEvent.LoadData -> loadData()
            is NotificationSettingEvent.UpdateAlertSoundFocus -> updateAlertSoundFocus(sound = event.sound)
            is NotificationSettingEvent.UpdateAlertSoundPause -> updateAlertSoundPause(sound = event.sound)
            is NotificationSettingEvent.UpdateVibrationEnabled -> updateVibrationEnabled(isEnabled = event.isEnabled)
            NotificationSettingEvent.CloseModal -> closeModal()
            is NotificationSettingEvent.ShowModal -> showModal(modal = event.modal)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            pomodoroSettingRepository.pomodoroSetting.collect { pomodoroSettingDomain ->
                _uiState.update { state ->
                    state.copy(
                        alertSoundFocus = pomodoroSettingDomain.alertSoundFocus,
                        alertSoundPause = pomodoroSettingDomain.alertSoundPause,
                        isVibrationEnabled = pomodoroSettingDomain.isVibrationEnabled
                    )
                }
            }
        }
    }

    private fun updateVibrationEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            pomodoroSettingRepository.updateIsVibrationEnabled(isEnabled = isEnabled)
        }
    }

    private fun updateAlertSoundFocus(sound: SoundAlarm?) {
        backgroundSoundPlayer.stop()
        sound?.let {
            soundAlertPreview(soundRes = sound.soundRes)
        }

        viewModelScope.launch {
            pomodoroSettingRepository.updateAlertSoundFocus(sound = sound)
        }

    }

    private fun updateAlertSoundPause(sound: SoundAlarm?) {
        backgroundSoundPlayer.stop()
        sound?.let {
            soundAlertPreview(soundRes = sound.soundRes)
        }
        viewModelScope.launch {
            pomodoroSettingRepository.updateAlertSoundPause(sound = sound)
        }
    }

    private fun showModal(modal: NotificationSettingModalUiState) {
        _uiState.update { state ->
            state.copy(modal = modal)
        }
    }

    private fun closeModal() {
        _uiState.update { state ->
            state.copy(modal = NotificationSettingModalUiState.None)
        }
    }

    private fun soundAlertPreview(
        @RawRes soundRes: Int
    ) {
        backgroundSoundPlayer.play(resId = soundRes, volume = 1f, looping = false)
    }
}