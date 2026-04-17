package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import kotlinx.coroutines.flow.Flow

interface PomodoroTimeRepository {
    val pomodoroFlow: Flow<PomodoroTimePreferences>

    suspend fun start()

    suspend fun pause()

    suspend fun play()

    suspend fun reset()

    suspend fun restart()

    suspend fun skip()

    suspend fun currentTask(id: Int?)

    fun getRemaining(state: PomodoroTimePreferences): Long

    suspend fun finishedSessionPomodoro()
}