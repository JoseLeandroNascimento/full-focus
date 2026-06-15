package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.screen.config_sound.TabConfigSound

sealed interface ConfigSoundEvent {

    data object OnLoad: ConfigSoundEvent

    data class OnSelectTab(val tab: TabConfigSound) : ConfigSoundEvent

    data class ChangeVolume(val volume: Int) : ConfigSoundEvent

    data class ChangeSound(val sound: SoundBackground) : ConfigSoundEvent

    data object ResetVolume : ConfigSoundEvent

    data object StopPreview : ConfigSoundEvent
}