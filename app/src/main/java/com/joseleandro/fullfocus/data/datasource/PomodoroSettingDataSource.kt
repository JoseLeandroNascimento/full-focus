package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingDataSource {

    val pomodoroSetting: Flow<PomodoroSetting>

    suspend fun updateFocusTime(time: Long)

    suspend fun updateShortBreakTime(time: Long)

    suspend fun updateLongBreakTime(time: Long)

}