package com.joseleandro.fullfocus.data.datasource

import androidx.datastore.core.DataStore
import com.joseleandro.fullfocus.data.local.preferences.UserPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PomodoroSettingPreferencesLocalDataSourceImpl(
    private val dataStore: DataStore<UserPreferences>
) : PomodoroSettingPreferencesLocalDataSource {

    override val pomodoroSetting: Flow<PomodoroSettingPreferences>
        get() = dataStore.data.map { it.pomodoroSettingPreferences }

    override suspend fun updatePomodoroSetting(pomodoroSetting: PomodoroSettingPreferences) {

        dataStore.updateData {
            it.copy(
                pomodoroSettingPreferences = pomodoroSetting
            )
        }

    }
}