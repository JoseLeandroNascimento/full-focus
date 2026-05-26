package com.joseleandro.fullfocus.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSource
import com.joseleandro.fullfocus.data.local.mapper.toDomain
import com.joseleandro.fullfocus.domain.model.PomodoroSettingDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PomodoroSettingRepositoryImpl(
    private val pomodoroSettingDataSource: PomodoroSettingDataSource
) : PomodoroSettingRepository {

    override val pomodoroSetting: Flow<PomodoroSettingDomain>
        get() = pomodoroSettingDataSource.pomodoroSetting.map { it.toDomain() }

    override suspend fun updateFocusTime(time: Long) {
        pomodoroSettingDataSource.updateFocusTime(time = time)
    }

    override suspend fun updateShortBreakTime(time: Long) {
        pomodoroSettingDataSource.updateShortBreakTime(time = time)
    }

    override suspend fun updateLongBreakTime(time: Long) {
        pomodoroSettingDataSource.updateLongBreakTime(time = time)
    }

    override suspend fun updateFocusProgressColor(color: Color) {
        pomodoroSettingDataSource.updateFocusProgressColor(color = color.toColorLong())
    }

    override suspend fun updateShortBreakProgressColor(color: Color) {
        pomodoroSettingDataSource.updateShortBreakProgressColor(color = color.toColorLong())
    }

    override suspend fun updateLongBreakProgressColor(color: Color) {
        pomodoroSettingDataSource.updateLongBreakProgressColor(color = color.toColorLong())
    }
}