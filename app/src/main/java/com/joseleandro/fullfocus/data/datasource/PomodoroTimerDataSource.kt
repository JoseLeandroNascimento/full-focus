package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.model.PomodoroTimer
import kotlinx.coroutines.flow.Flow

interface PomodoroTimerDataSource {

    val pomodoroTimer: Flow<PomodoroTimer>

    suspend fun start()

    suspend fun pause()

    suspend fun cancel()

    suspend fun restart()

    suspend fun getTime(): Long
}