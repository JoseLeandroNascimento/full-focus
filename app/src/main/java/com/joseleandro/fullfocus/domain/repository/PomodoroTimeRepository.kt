package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.data.PomodoroTimeEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PomodoroTimeRepository {
    val pomodoroFlow: Flow<PomodoroTimePreferences>
    val events: SharedFlow<PomodoroTimeEvent>

    suspend fun start()

    suspend fun pause()

    suspend fun play()

    suspend fun reset()

    suspend fun restart()

    suspend fun skip()

    suspend fun currentTask(id: Int?)

    fun getRemaining(state: PomodoroTimePreferences): Long

    suspend fun onPomodoroSessionFinished()
}