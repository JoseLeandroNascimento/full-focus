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

    override suspend fun updateFocusTime(time: Long) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    focusTime = time
                )
            )
        }
    }

    override suspend fun updateShortBreakTime(time: Long) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    shortPauseTime = time
                )
            )
        }
    }

    override suspend fun updateLongBreakTime(time: Long) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    longPauseTime = time
                )
            )
        }
    }

    override suspend fun updateFocusProgressColor(color: Long) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    focusProgressColor = color
                )
            )
        }
    }

    override suspend fun updateShortBreakProgressColor(color: Long) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    shortBreakProgressColor = color
                )
            )
        }
    }

    override suspend fun updateLongBreakProgressColor(color: Long) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    longBreakProgressColor = color
                )
            )
        }
    }
}