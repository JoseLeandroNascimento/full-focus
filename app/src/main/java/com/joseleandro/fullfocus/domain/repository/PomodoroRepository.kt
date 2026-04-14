package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroCurrentPreferences
import kotlinx.coroutines.flow.Flow

interface PomodoroRepository {
    val pomodoroFlow: Flow<PomodoroCurrentPreferences>

    suspend fun start(duration: Long)

    suspend fun pause()

    suspend fun play()

    suspend fun resume()

    suspend fun reset()

    fun getRemaining(state: PomodoroCurrentPreferences): Long
}