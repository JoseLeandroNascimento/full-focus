package com.joseleandro.fullfocus.data.datasource

import android.content.Context
import com.joseleandro.fullfocus.data.local.preferences.dataStore
import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PomodoroSettingDataSourceImpl(
    private val context: Context
) : PomodoroSettingDataSource {

    override val pomodoroSetting: Flow<PomodoroSetting>
        get() = context.dataStore.data.map { it.pomodoroSetting }

    override suspend fun updatePomodoroSetting(
        focusTime: Long,
        shortPauseTime: Long,
        longPauseTime: Long
    ) {
        context.dataStore.updateData { setting ->
            setting.copy(
                pomodoroSetting = setting.pomodoroSetting.copy(
                    focusTime = focusTime,
                    shortPauseTime = shortPauseTime,
                    longPauseTime = longPauseTime
                )
            )
        }
    }
}
