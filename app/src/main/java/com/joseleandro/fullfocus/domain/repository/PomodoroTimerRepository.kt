package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.model.PomodoroTimerDomain
import kotlinx.coroutines.flow.Flow

interface PomodoroTimerRepository {

    val pomodoroTimer: Flow<PomodoroTimerDomain>

    suspend fun play()

    suspend fun pause()

    suspend fun cancel()

    suspend fun restart()

    suspend fun updateTimer()
}