package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroSettingPreferencesLocalDataSource
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingPreferencesRepository
import kotlinx.coroutines.flow.Flow

class PomodoroSettingPreferencesRepositoryImpl(
    private val pomodoroSettingDataSource: PomodoroSettingPreferencesLocalDataSource
) : PomodoroSettingPreferencesRepository {

    override val pomodoroSettingPreferences: Flow<PomodoroSettingPreferences>
        get() = pomodoroSettingDataSource.pomodoroSetting

    override suspend fun updatePomodoroSetting(pomodoroSetting: PomodoroSettingPreferences) {
        pomodoroSettingDataSource.updatePomodoroSetting(pomodoroSetting)
    }

}