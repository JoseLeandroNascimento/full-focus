package com.joseleandro.fullfocus.data.repository

import androidx.datastore.core.DataStore
import com.joseleandro.fullfocus.data.local.preferences.UserPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroPreferences
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class PomodoroRepositoryImpl(
    private val dataStore: DataStore<UserPreferences>
) : PomodoroRepository {

    override val pomodoroFlow: Flow<PomodoroPreferences> =
        dataStore.data.map { it.pomodoro }

    override suspend fun start(duration: Long) {
        val now = System.currentTimeMillis()

        dataStore.updateData {
            it.copy(
                pomodoro = PomodoroPreferences(
                    startTime = now,
                    duration = duration,
                    isRunning = true
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
                    isRunning = false
                )
            )
        }
    }

    override suspend fun resume() {
        val now = System.currentTimeMillis()

        val current = dataStore.data.first()
        val state = current.pomodoro

        val pausedDuration = now - (state.pausedAt ?: now)
        val newStart = state.startTime + pausedDuration

        dataStore.updateData {
            it.copy(
                pomodoro = state.copy(
                    startTime = newStart,
                    pausedAt = null,
                    isRunning = true
                )
            )
        }
    }

    override suspend fun reset() {
        dataStore.updateData {
            it.copy(
                pomodoro = PomodoroPreferences()
            )
        }
    }

    override fun getRemaining(state: PomodoroPreferences): Long {
        if (!state.isRunning) return state.duration

        val now = System.currentTimeMillis()
        val elapsed = now - state.startTime

        return (state.duration - elapsed).coerceAtLeast(0)
    }
}
