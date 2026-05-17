package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingRepository {
    val pomodoroSetting: Flow<PomodoroSetting>

    suspend fun updatePomodoroSetting(
        focusTime: Long,
        shortPauseTime: Long,
        longPauseTime: Long
    )
}
