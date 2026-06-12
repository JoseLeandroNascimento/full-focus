package com.joseleandro.fullfocus.domain.repository

import androidx.compose.ui.graphics.Color
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.domain.model.PomodoroSettingDomain
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingRepository {

    val pomodoroSetting: Flow<PomodoroSettingDomain>

    suspend fun updateFocusTime(time: Long)

    suspend fun updateShortBreakTime(time: Long)

    suspend fun updateLongBreakTime(time: Long)

    suspend fun updateFocusProgressColor(color: Color)

    suspend fun updateShortBreakProgressColor(color: Color)

    suspend fun updateLongBreakProgressColor(color: Color)

    suspend fun updateVolumeSoundFocus(volume: Int)

    suspend fun updateVolumeSoundPause(volume: Int)

    suspend fun updateSoundFocus(sound: SoundBackground)

    suspend fun updateSoundPause(sound: SoundBackground)

    suspend fun updateIsSoundEnabled(isEnabled: Boolean)
}