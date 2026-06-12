package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingDataSource {

    val pomodoroSetting: Flow<PomodoroSetting>

    suspend fun updateFocusTime(time: Long)

    suspend fun updateShortBreakTime(time: Long)

    suspend fun updateLongBreakTime(time: Long)

    suspend fun updateFocusProgressColor(color: Long)

    suspend fun updateShortBreakProgressColor(color: Long)

    suspend fun updateLongBreakProgressColor(color: Long)

    suspend fun updateVolumeSoundFocus(volume: Int)

    suspend fun updateVolumeSoundPause(volume: Int)

    suspend fun updateSoundFocus(sound: SoundBackground)

    suspend fun updateSoundPause(sound: SoundBackground)

    suspend fun updateIsSoundEnabled(isEnabled: Boolean)

}