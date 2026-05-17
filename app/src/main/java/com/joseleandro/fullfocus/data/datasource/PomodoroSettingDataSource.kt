package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingDataSource {
    val pomodoroSetting: Flow<PomodoroSetting>

    suspend fun updatePomodoroSetting(
        focusTime: Long,
        shortPauseTime: Long,
        longPauseTime: Long
    )
}
