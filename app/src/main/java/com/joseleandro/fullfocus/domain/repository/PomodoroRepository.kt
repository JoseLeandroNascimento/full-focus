package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.model.PomodoroDomain
import kotlinx.coroutines.flow.Flow

interface PomodoroRepository {

    val pomodoro: Flow<PomodoroDomain>

    suspend fun play()

    suspend fun pause()

    suspend fun reverse()

    suspend fun cancelAndSave()

    suspend fun cancelAndDelete()

    suspend fun skip()

    suspend fun completeSession()
}
