package com.joseleandro.fullfocus.data.datasource

import androidx.datastore.core.DataStore
import com.joseleandro.fullfocus.data.local.preferences.UserPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroSettingPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession
import com.joseleandro.fullfocus.domain.data.PomodoroTimeEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PomodoroTimePreferenceDataSourceImpl(
    private val dataStore: DataStore<UserPreferences>,
    private val pomodoroSettingPreferencesLocalDataSource: PomodoroSettingPreferencesLocalDataSource,
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

    private val _events = MutableSharedFlow<PomodoroTimeEvent>(
        replay = 0,
        extraBufferCapacity = 10
    )
    override val events = _events.asSharedFlow()


    override suspend fun start() {
        val now = System.currentTimeMillis()

        val response = dataStore.updateData {
            it.copy(
                pomodoro = it.pomodoro.copy(
                    startTime = now,
                    pausedAt = null,
                    isRunning = true,
                )
            )
        }

        emit(PomodoroTimeEvent.Start(sessionInfo = response.pomodoro))
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

        emit(PomodoroTimeEvent.Pause)
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

        emit(PomodoroTimeEvent.Play)
    }

    override suspend fun reset() {
        dataStore.updateData {
            it.copy(
                pomodoro = PomodoroTimePreferences()
            )
        }
        emit(PomodoroTimeEvent.Reset)
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

        emit(PomodoroTimeEvent.Skip)
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


    override suspend fun onPomodoroSessionFinished() {
        val current = pomodoroFlow.first()

        if (current.startTime == 0L) return

        val result = dataStore.updateData { userPreferences ->

            userPreferences.copy(
                pomodoro = userPreferences.pomodoro.copy(
                    isRunning = false,
                    statusSession = userPreferences.pomodoro.statusSession.nextSession(),
                    counterPomodoro = incrementSessionPomodoroCompleted(userPreferences.pomodoro),
                    startTime = 0L,
                    pausedAt = 0L
                )
            )
        }

        emit(
            PomodoroTimeEvent.PomodoroTimeCompleted(
                count = result.pomodoro.counterPomodoro
            )
        )

    }

    private suspend fun StatusSession.nextSession(): StatusSession {
        val state = pomodoroFlow.first()

        return when (this) {
            StatusSession.FOCUS -> {
                val interval = state.pomodoroIntervalBreakLong

                if (interval > 0 && (state.counterPomodoro + 1) % interval == 0)
                    StatusSession.PAUSE_LONG
                else
                    StatusSession.PAUSE_SHORT
            }

            StatusSession.PAUSE_SHORT,
            StatusSession.PAUSE_LONG -> {
                StatusSession.FOCUS
            }
        }
    }

    private fun incrementSessionPomodoroCompleted(
        pomodoro: PomodoroTimePreferences
    ): Int {
        return if (
            pomodoro.statusSession == StatusSession.PAUSE_SHORT ||
            pomodoro.statusSession == StatusSession.PAUSE_LONG
        ) {
            pomodoro.counterPomodoro + 1
        } else {
            pomodoro.counterPomodoro
        }
    }


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

    private fun emit(event: PomodoroTimeEvent) =
        _events.tryEmit(event)
}