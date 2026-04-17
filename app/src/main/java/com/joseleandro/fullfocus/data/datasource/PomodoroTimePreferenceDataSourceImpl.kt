package com.joseleandro.fullfocus.data.datasource

import androidx.datastore.core.DataStore
import com.joseleandro.fullfocus.data.local.preferences.UserPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.data.local.preferences.data.StatusSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PomodoroTimePreferenceDataSourceImpl(
    private val dataStore: DataStore<UserPreferences>,
    private val pomodoroSettingPreferencesLocalDataSource: PomodoroSettingPreferencesLocalDataSource
) : PomodoroTimePreferenceDataSource {

    override val pomodoroFlow: Flow<PomodoroTimePreferences> =
        combine(
            pomodoroSettingPreferencesLocalDataSource.pomodoroSetting,
            dataStore.data.map { it.pomodoro }
        ) { pomodoroSetting, pomodoroCurrent ->
            pomodoroCurrent.copy(
                duration = durationTimeCurrentStatusPomodoro(
                    pomodoroSetting = pomodoroSetting,
                    pomodoroTimePreferences = pomodoroCurrent
                ),
                pomodoroIntervalBreakLong = pomodoroSetting.longBreakInterval
            )
        }


    override suspend fun start() {
        val now = System.currentTimeMillis()

        dataStore.updateData {
            it.copy(
                pomodoro = it.pomodoro.copy(
                    startTime = now,
                    pausedAt = null,
                    isRunning = true,
                )
            )
        }
    }

    override suspend fun pause() {
        val now = System.currentTimeMillis()

        dataStore.updateData {
            it.copy(
                pomodoro = it.pomodoro.copy(
                    pausedAt = now,
                    isRunning = false,
                )
            )
        }
    }


    override suspend fun play() {
        val now = System.currentTimeMillis()
        dataStore.updateData {
            val state = it.pomodoro
            val pausedDuration = if (state.pausedAt != null) now - state.pausedAt else 0L

            it.copy(
                pomodoro = state.copy(
                    startTime = state.startTime + pausedDuration,
                    pausedAt = null,
                    isRunning = true
                )
            )
        }
    }

    override suspend fun reset() {
        dataStore.updateData {
            it.copy(
                pomodoro = PomodoroTimePreferences()
            )
        }
    }

    override suspend fun restart() {
        dataStore.updateData {
            it.copy(
                pomodoro = it.pomodoro.copy(
                    startTime = 0,
                    pausedAt = null,
                    isRunning = false
                )
            )
        }
    }

    override suspend fun skip() {
        dataStore.updateData {
            val state = it.pomodoro

            it.copy(
                pomodoro = state.copy(
                    startTime = 0,
                    pausedAt = null,
                    isRunning = false,
                    counterPomodoro = incrementSessionPomodoroCompleted(it.pomodoro),
                    statusSession = state.statusSession.nextSession()
                )
            )
        }
    }

    override suspend fun currentTask(id: Int?) {
        dataStore.updateData {
            it.copy(
                pomodoro = it.pomodoro.copy(idTask = id)
            )
        }
    }

    override fun getRemaining(state: PomodoroTimePreferences): Long {
        if (state.idTask == null) return 0L
        if (state.startTime == 0L) return state.duration
        val now = System.currentTimeMillis()

        return if (state.isRunning) {
            (state.duration - (now - state.startTime)).coerceAtLeast(0L)
        } else {
            val endPoint = state.pausedAt ?: now
            (state.duration - (endPoint - state.startTime)).coerceAtLeast(0L)
        }
    }


    override suspend fun finishedSessionPomodoro() {
        dataStore.updateData {
            it.copy(
                pomodoro = it.pomodoro.copy(
                    isRunning = false,
                    statusSession = it.pomodoro.statusSession.nextSession(),
                    counterPomodoro = incrementSessionPomodoroCompleted(it.pomodoro),
                    startTime = 0L,
                    pausedAt = 0L
                )
            )
        }
    }

    private suspend fun StatusSession.nextSession(): StatusSession {
        val pomodoroTimePreferences = pomodoroFlow.first()
        return when (this) {
            StatusSession.FOCUS -> {
                val interval = pomodoroTimePreferences.pomodoroIntervalBreakLong
                if (interval > 0 && (pomodoroTimePreferences.counterPomodoro + 1) % interval == 0)
                    StatusSession.PAUSE_LONG
                else
                    StatusSession.PAUSE_SHORT
            }

            else -> StatusSession.FOCUS
        }
    }

    private fun incrementSessionPomodoroCompleted(pomodoroTimePreferences: PomodoroTimePreferences): Int =
        if (pomodoroTimePreferences.statusSession !== StatusSession.FOCUS) pomodoroTimePreferences.counterPomodoro else pomodoroTimePreferences.counterPomodoro + 1


    private fun durationTimeCurrentStatusPomodoro(
        pomodoroSetting: PomodoroSettingPreferences,
        pomodoroTimePreferences: PomodoroTimePreferences
    ): Long {

        val statusSession = pomodoroTimePreferences.statusSession

        return when (statusSession) {
            StatusSession.FOCUS -> pomodoroSetting.pomodoroDuration
            StatusSession.PAUSE_SHORT -> pomodoroSetting.shortBreakDuration
            StatusSession.PAUSE_LONG -> pomodoroSetting.longBreakDuration
        }

    }
}