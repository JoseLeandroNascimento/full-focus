package com.joseleandro.fullfocus.data.datasource

import android.content.Context
import com.joseleandro.fullfocus.data.local.preferences.dataStore
import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroTimer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PomodoroTimerDataSourceImpl(
    private val context: Context
) : PomodoroTimerDataSource {


    override val pomodoroTimer: Flow<PomodoroTimer>
        get() = context.dataStore.data.map { setting ->
            setting.pomodoroTimer
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
                    endTime = null
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

    override suspend fun getTime(): Long {

        val pomodoroTimer = context.dataStore.data.first().pomodoroTimer

        if (pomodoroTimer.startTime == 0L) return pomodoroTimer.duration

        val now = System.currentTimeMillis()

        val referenceTime = pomodoroTimer.endTime ?: now

        val elapsed = referenceTime - pomodoroTimer.startTime

        return maxOf(0L, pomodoroTimer.duration - elapsed)

    }
}