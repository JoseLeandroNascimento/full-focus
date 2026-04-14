package com.joseleandro.fullfocus.data.repository

import androidx.datastore.core.DataStore
import com.joseleandro.fullfocus.data.local.preferences.UserPreferences
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroCurrentPreferences
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PomodoroRepositoryImpl(
    private val dataStore: DataStore<UserPreferences>
) : PomodoroRepository {

    override val pomodoroFlow: Flow<PomodoroCurrentPreferences> =
        dataStore.data.map { it.pomodoro }

    override suspend fun start(duration: Long) {
        val now = System.currentTimeMillis()

        dataStore.updateData {
            it.copy(
                pomodoro = PomodoroCurrentPreferences(
                    startTime = now,
                    duration = duration,
                    isRunning = true,
                    pausedAt = null
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
        resume()
    }

    override suspend fun resume() {
        val now = System.currentTimeMillis()
        dataStore.updateData {
            val state = it.pomodoro
            val pausedDuration = if (state.pausedAt != null) now - state.pausedAt else 0L

            it.copy(
                pomodoro = state.copy(
                    startTime = state.startTime + pausedDuration,
                    pausedAt = null, // Crucial limpar aqui
                    isRunning = true
                )
            )
        }
    }

    override suspend fun reset() {
        dataStore.updateData {
            it.copy(
                pomodoro = PomodoroCurrentPreferences()
            )
        }
    }

    override fun getRemaining(state: PomodoroCurrentPreferences): Long {
        if (state.startTime == 0L) return state.duration
        val now = System.currentTimeMillis()

        return if (state.isRunning) {
            (state.duration - (now - state.startTime)).coerceAtLeast(0L)
        } else {
            val endPoint = state.pausedAt ?: now
            (state.duration - (endPoint - state.startTime)).coerceAtLeast(0L)
        }
    }
}
