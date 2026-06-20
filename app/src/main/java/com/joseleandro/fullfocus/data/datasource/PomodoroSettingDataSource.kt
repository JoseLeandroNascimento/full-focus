package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.theme.ColorStyle
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingDataSource {

    val pomodoroSetting: Flow<PomodoroSetting>

    suspend fun updateFocusTime(time: Long)

    suspend fun updateShortBreakTime(time: Long)

    suspend fun updateLongBreakTime(time: Long)

    suspend fun updateFocusProgressColor(color: ColorStyle)

    suspend fun updateShortBreakProgressColor(color: ColorStyle)

    suspend fun updateLongBreakProgressColor(color: ColorStyle)

    suspend fun updateVolumeSoundFocus(volume: Int)

    suspend fun updateVolumeSoundPause(volume: Int)

    suspend fun updateSoundFocus(sound: SoundBackground)

    suspend fun updateSoundPause(sound: SoundBackground)

    suspend fun updateIsSoundEnabled(isEnabled: Boolean)

}