package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingPreferencesLocalDataSource {

    val pomodoroSetting: Flow<PomodoroSettingPreferences>

    suspend fun updatePomodoroSetting(pomodoroSetting: PomodoroSettingPreferences)

}