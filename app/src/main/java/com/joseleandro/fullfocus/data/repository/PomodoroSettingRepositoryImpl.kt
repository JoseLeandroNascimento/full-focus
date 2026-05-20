package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSource
import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import kotlinx.coroutines.flow.Flow

class PomodoroSettingRepositoryImpl(
    private val pomodoroSettingDataSource: PomodoroSettingDataSource
) : PomodoroSettingRepository {

    override val pomodoroSetting: Flow<PomodoroSetting>
        get() = pomodoroSettingDataSource.pomodoroSetting

    override suspend fun updateFocusTime(time: Long) {
        pomodoroSettingDataSource.updateFocusTime(time = time)
    }

    override suspend fun updateShortBreakTime(time: Long) {
        pomodoroSettingDataSource.updateShortBreakTime(time = time)
    }

    override suspend fun updateLongBreakTime(time: Long) {
        pomodoroSettingDataSource.updateLongBreakTime(time = time)
    }
}