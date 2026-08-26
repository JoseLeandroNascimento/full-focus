package com.joseleandro.fullfocus.data.datasource

import android.content.Context
import com.joseleandro.fullfocus.data.local.preferences.dataStore
import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroSetting
import com.joseleandro.fullfocus.data.local.preferences.model.SoundAlarm
import com.joseleandro.fullfocus.data.local.preferences.model.SoundBackground
import com.joseleandro.fullfocus.ui.theme.ColorStyle
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

    override suspend fun updateFocusProgressColor(color: ColorStyle) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    focusProgressColor = color
                )
            )
        }
    }

    override suspend fun updateShortBreakProgressColor(color: ColorStyle) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    shortBreakProgressColor = color
                )
            )
        }
    }

    override suspend fun updateLongBreakProgressColor(color: ColorStyle) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    longBreakProgressColor = color
                )
            )
        }
    }

    override suspend fun updateVolumeSoundFocus(volume: Int) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    volumeFocus = volume
                )
            )
        }
    }

    override suspend fun updateVolumeSoundPause(volume: Int) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    volumePause = volume
                )
            )
        }
    }

    override suspend fun updateSoundFocus(sound: SoundBackground) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    soundFocus = sound
                )
            )
        }
    }

    override suspend fun updateSoundPause(sound: SoundBackground) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    soundPause = sound
                )
            )
        }
    }

    override suspend fun updateIsSoundEnabled(isEnabled: Boolean) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    isSoundEnabled = isEnabled
                )
            )
        }
    }

    override suspend fun updateIsVibrationEnabled(isEnabled: Boolean) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    isVibrationEnabled = isEnabled
                )
            )
        }
    }

    override suspend fun updateAlertSoundFocus(sound: SoundAlarm?) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    alertSoundFocus = sound
                )
            )
        }
    }

    override suspend fun updateAlertSoundPause(sound: SoundAlarm?) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    alertSoundPause = sound
                )
            )
        }
    }

    override suspend fun updateDailyGoal(goal: Int) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    dailyGoal = goal
                )
            )
        }
    }

    override suspend fun updateWeeklyGoal(goal: Int) {
        context.dataStore.updateData { state ->
            state.copy(
                pomodoroSetting = state.pomodoroSetting.copy(
                    weeklyGoal = goal
                )
            )
        }
    }
}