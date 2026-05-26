package com.joseleandro.fullfocus.domain.repository

import androidx.compose.ui.graphics.Color
import com.joseleandro.fullfocus.domain.model.PomodoroSettingDomain
import kotlinx.coroutines.flow.Flow

interface PomodoroSettingRepository {

    val pomodoroSetting: Flow<PomodoroSettingDomain>

    suspend fun updateFocusTime(time: Long)

    suspend fun updateShortBreakTime(time: Long)

    suspend fun updateLongBreakTime(time: Long)

    suspend fun updateFocusProgressColor(color: Color)

    suspend fun updateShortBreakProgressColor(color: Color)

    suspend fun updateLongBreakProgressColor(color: Color)
}