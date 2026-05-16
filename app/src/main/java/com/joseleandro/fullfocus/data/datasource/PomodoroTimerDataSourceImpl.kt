package com.joseleandro.fullfocus.data.datasource

import android.content.Context
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.preferences.dataStore
import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroTimer
import com.joseleandro.fullfocus.data.local.preferences.model.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PomodoroTimerDataSourceImpl(
    private val context: Context
) : PomodoroTimerDataSource {


    override val pomodoroTimer: Flow<PomodoroTimer>
        get() = context.dataStore.data.map { setting ->
            setting.pomodoroTimer.copy(
                duration = setting.calcDurationSessionPomodoro()
            )
        }

    override suspend fun start() {
        context.dataStore.updateData { setting ->

            val now = System.currentTimeMillis()

            val startTime =
                if (setting.pomodoroTimer.startTime == 0L) {
                    now
                } else {
                    val pauseDuration = now - (setting.pomodoroTimer.endTime ?: now)
                    setting.pomodoroTimer.startTime + pauseDuration
                }

            setting.copy(
                pomodoroTimer = setting.pomodoroTimer.copy(
                    isRunning = true,
                    startTime = startTime,
                    endTime = null,
                    progress = true
                )
            )
        }
    }

    override suspend fun pause() {
        context.dataStore.updateData { setting ->
            setting.copy(
                pomodoroTimer = setting.pomodoroTimer.copy(
                    isRunning = false,
                    endTime = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun cancel() {

        context.dataStore.updateData { setting ->
            setting.copy(
                pomodoroTimer = PomodoroTimer()
            )
        }
    }

    override suspend fun restart() {

        context.dataStore.updateData { setting ->
            setting.copy(
                pomodoroTimer = setting.pomodoroTimer.copy(
                    isRunning = false,
                    startTime = 0L,
                    endTime = null
                )
            )
        }
    }

    override suspend fun getTime(): Long {

        val setting = context.dataStore.data.first()
        val pomodoroTimer = setting.pomodoroTimer
        val duration = setting.calcDurationSessionPomodoro()

        if (pomodoroTimer.startTime == 0L) return duration

        val now = System.currentTimeMillis()

        val referenceTime = pomodoroTimer.endTime ?: now

        val elapsed = referenceTime - pomodoroTimer.startTime

        return maxOf(0L, duration - elapsed)

    }

    private fun Setting.calcDurationSessionPomodoro(): Long =

        when (pomodoroTimer.pomodoroState) {
            PomodoroState.FOCUS -> pomodoroSetting.focusTime
            PomodoroState.SHORT_PAUSE -> pomodoroSetting.shortPauseTime
            PomodoroState.LONG_PAUSE -> pomodoroSetting.longPauseTime
        }

}