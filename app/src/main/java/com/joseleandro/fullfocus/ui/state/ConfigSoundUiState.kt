package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.screen.config_sound.TabConfigSound

data class ConfigSoundUiState(
    val currentVolume: Int = 0,
    val selectedSound: SoundBackground? = null,
    val selectedTab: TabConfigSound = TabConfigSound.FOCUS_OPTIONS,
    val isPreviewPlaying: Boolean = false
)
