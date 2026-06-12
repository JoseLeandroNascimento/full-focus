package com.joseleandro.fullfocus.ui.screen.config_sound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(TabConfigSound.FOCUS_OPTIONS)

    // Observa apenas as propriedades de som para evitar recomposições inúteis
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
        _selectedTab
    ) { settings, selectedTab ->
        val isFocus = selectedTab == TabConfigSound.FOCUS_OPTIONS
        ConfigSoundUiState(
            currentVolume = if (isFocus) settings.volumeFocus else settings.volumePause,
            selectedSound = if (isFocus) settings.soundFocus else settings.soundPause,
            selectedTab = selectedTab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ConfigSoundUiState()
    )

    fun onEvent(event: ConfigSoundEvent) {
        when (event) {
            is ConfigSoundEvent.OnSelectTab -> _selectedTab.value = event.tab
            is ConfigSoundEvent.ChangeSound -> updateSound(event.sound)
            is ConfigSoundEvent.ChangeVolume -> updateVolume(event.volume)
            ConfigSoundEvent.ResetVolume -> resetVolume()
            ConfigSoundEvent.OnLoad -> {}
        }
    }

    private fun resetVolume() {
        val defaultVolume = if (_selectedTab.value == TabConfigSound.FOCUS_OPTIONS) 70 else 50
        updateVolume(defaultVolume)
    }

    private fun updateVolume(volume: Int) {
        viewModelScope.launch {
            when (_selectedTab.value) {
                TabConfigSound.FOCUS_OPTIONS -> pomodoroSettingRepository.updateVolumeSoundFocus(volume)
                TabConfigSound.PAUSE_OPTIONS -> pomodoroSettingRepository.updateVolumeSoundPause(volume)
            }
        }
    }

    private fun updateSound(sound: SoundBackground) {
        viewModelScope.launch {
            when (_selectedTab.value) {
                TabConfigSound.FOCUS_OPTIONS -> pomodoroSettingRepository.updateSoundFocus(sound)
                TabConfigSound.PAUSE_OPTIONS -> pomodoroSettingRepository.updateSoundPause(sound)
            }
        }
    }

    // Classe interna auxiliar para agrupar apenas o que importa do repositório
    private data class SoundSettings(
        val volumeFocus: Int,
        val volumePause: Int,
        val soundFocus: SoundBackground?,
        val soundPause: SoundBackground?
    )
}
