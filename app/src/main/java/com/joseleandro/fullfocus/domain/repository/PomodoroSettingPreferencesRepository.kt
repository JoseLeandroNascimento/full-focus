package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingPreferencesRepository {

    val pomodoroSettingPreferences: Flow<PomodoroSettingPreferences>

    suspend fun updatePomodoroSetting(pomodoroSetting: PomodoroSettingPreferences)

}