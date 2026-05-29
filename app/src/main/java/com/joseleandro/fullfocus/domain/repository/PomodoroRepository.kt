package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.effect.PomodoroEffect
import com.joseleandro.fullfocus.domain.model.PomodoroDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PomodoroRepository {

    val pomodoro: Flow<PomodoroDomain>

    val effect: SharedFlow<PomodoroEffect>

    suspend fun play()

    suspend fun pause()

    suspend fun reverse()

    suspend fun cancelAndSave()

    suspend fun cancelAndDelete()

    suspend fun skip()

    suspend fun completeSession()
}
