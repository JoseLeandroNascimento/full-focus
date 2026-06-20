package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSource
import com.joseleandro.fullfocus.data.local.mapper.toDomain
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.domain.model.PomodoroSettingDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.ui.theme.ColorStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PomodoroSettingRepositoryImpl(
    private val pomodoroSettingDataSource: PomodoroSettingDataSource
) : PomodoroSettingRepository {

    override val pomodoroSetting: Flow<PomodoroSettingDomain>
        get() = pomodoroSettingDataSource.pomodoroSetting.map { it.toDomain() }

    override suspend fun updateFocusTime(time: Long) {
        pomodoroSettingDataSource.updateFocusTime(time = time)
    }

    override suspend fun updateShortBreakTime(time: Long) {
        pomodoroSettingDataSource.updateShortBreakTime(time = time)
    }

    override suspend fun updateLongBreakTime(time: Long) {
        pomodoroSettingDataSource.updateLongBreakTime(time = time)
    }

    override suspend fun updateFocusProgressColor(color: ColorStyle) {
        pomodoroSettingDataSource.updateFocusProgressColor(color = color)
    }

    override suspend fun updateShortBreakProgressColor(color: ColorStyle) {
        pomodoroSettingDataSource.updateShortBreakProgressColor(color = color)
    }

    override suspend fun updateLongBreakProgressColor(color: ColorStyle) {
        pomodoroSettingDataSource.updateLongBreakProgressColor(color = color)
    }

    override suspend fun updateVolumeSoundFocus(volume: Int) {
        pomodoroSettingDataSource.updateVolumeSoundFocus(volume = volume)
    }

    override suspend fun updateVolumeSoundPause(volume: Int) {
        pomodoroSettingDataSource.updateVolumeSoundPause(volume = volume)
    }

    override suspend fun updateSoundFocus(sound: SoundBackground) {
        pomodoroSettingDataSource.updateSoundFocus(sound = sound)
    }

    override suspend fun updateSoundPause(sound: SoundBackground) {
        pomodoroSettingDataSource.updateSoundPause(sound = sound)
    }

    override suspend fun updateIsSoundEnabled(isEnabled: Boolean) {
        pomodoroSettingDataSource.updateIsSoundEnabled(isEnabled = isEnabled)
    }
}