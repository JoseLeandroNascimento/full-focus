package com.joseleandro.fullfocus.ui.screen.config_sound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.core.util.BackgroundSoundPlayer
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.ui.event.ConfigSoundEvent
import com.joseleandro.fullfocus.ui.state.ConfigSoundUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfigSoundViewModel(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val backgroundSoundPlayer: BackgroundSoundPlayer,
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(TabConfigSound.FOCUS_OPTIONS)
    private val _isPreviewPlaying = MutableStateFlow(false)
    private val _localSelectedSound = MutableStateFlow<SoundBackground?>(null)

    private val _soundSettings = pomodoroSettingRepository.pomodoroSetting
        .map { setting ->
            SoundSettings(
                volumeFocus = setting.volumeFocus,
                volumePause = setting.volumePause,
                soundFocus = setting.soundFocus,
                soundPause = setting.soundPause
            )
        }.distinctUntilChanged()

    val uiState: StateFlow<ConfigSoundUiState> = combine(
        _soundSettings,
        _selectedTab,
        _isPreviewPlaying,
        _localSelectedSound
    ) { settings, selectedTab, isPreviewPlaying, localSelectedSound ->
        val isFocus = selectedTab == TabConfigSound.FOCUS_OPTIONS
        val remoteSound = if (isFocus) settings.soundFocus else settings.soundPause

        ConfigSoundUiState(
            currentVolume = if (isFocus) settings.volumeFocus else settings.volumePause,
            selectedSound = localSelectedSound ?: remoteSound,
            selectedTab = selectedTab,
            isPreviewPlaying = isPreviewPlaying
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ConfigSoundUiState()
    )

    fun onEvent(event: ConfigSoundEvent) {
        when (event) {
            is ConfigSoundEvent.OnSelectTab -> {
                selectTab(tab = event.tab)
            }

            is ConfigSoundEvent.ChangeSound -> {
                changeSound(sound = event.sound)
            }

            is ConfigSoundEvent.ChangeVolume -> {
                changeVolume(event.volume)
            }

            ConfigSoundEvent.ResetVolume -> resetVolume()
            ConfigSoundEvent.StopPreview -> stopPreview()
        }
    }

    private fun selectTab(tab: TabConfigSound) {
        _selectedTab.value = tab
        _localSelectedSound.value = null
        stopPreview()
    }

    private fun changeSound(sound: SoundBackground) {
        val currentSelected = uiState.value.selectedSound

        // Atualiza o estado local imediatamente para feedback visual
        _localSelectedSound.value = sound
        updateSound(sound)

        if (currentSelected == sound) {
            // Se já era o selecionado, alterna o preview
            if (_isPreviewPlaying.value) {
                stopPreview()
            } else {
                startPreview(sound)
            }
        } else {
            // Se é um diferente, seleciona e SEMPRE começa a tocar o preview direto
            startPreview(sound)
        }
    }

    private fun startPreview(sound: SoundBackground) {
        if (sound.soundRes != null) {
            val volume = uiState.value.currentVolume
            backgroundSoundPlayer.play(sound.soundRes, volume / 100f)
            _isPreviewPlaying.value = true
        } else {
            stopPreview()
        }
    }

    private fun stopPreview() {
        backgroundSoundPlayer.stop()
        _isPreviewPlaying.value = false
    }

    private fun changeVolume(volume: Int) {
        updateVolume(volume = volume)
        backgroundSoundPlayer.updateVolume(volume = volume / 100f)
    }

    private fun resetVolume() {
        val defaultVolume = if (_selectedTab.value == TabConfigSound.FOCUS_OPTIONS) 70 else 50
        updateVolume(defaultVolume)
        backgroundSoundPlayer.updateVolume(defaultVolume / 100f)
    }

    private fun updateVolume(volume: Int) {
        viewModelScope.launch {
            when (_selectedTab.value) {
                TabConfigSound.FOCUS_OPTIONS -> pomodoroSettingRepository.updateVolumeSoundFocus(
                    volume
                )

                TabConfigSound.BREAK_OPTIONS -> pomodoroSettingRepository.updateVolumeSoundPause(
                    volume
                )
            }
        }
    }

    private fun updateSound(sound: SoundBackground) {
        viewModelScope.launch {
            when (_selectedTab.value) {
                TabConfigSound.FOCUS_OPTIONS -> pomodoroSettingRepository.updateSoundFocus(sound)
                TabConfigSound.BREAK_OPTIONS -> pomodoroSettingRepository.updateSoundPause(sound)
            }
        }
    }

    private data class SoundSettings(
        val volumeFocus: Int,
        val volumePause: Int,
        val soundFocus: SoundBackground?,
        val soundPause: SoundBackground?
    )

    override fun onCleared() {
        backgroundSoundPlayer.release()
    }
}
