package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.data.PomodoroTimeUIEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PomodoroTimePreferenceDataSource {

    val pomodoroFlow: Flow<PomodoroTimePreferences>
    val events: SharedFlow<PomodoroTimeUIEffect>

    suspend fun start()

    suspend fun pause()

    suspend fun play()

    suspend fun restart()

    suspend fun skip()

    suspend fun cancel(completed: Boolean = false)

    suspend fun currentTask(id: Int? = null)

    fun getRemaining(state: PomodoroTimePreferences): Long

    suspend fun onPomodoroSessionFinished()
}