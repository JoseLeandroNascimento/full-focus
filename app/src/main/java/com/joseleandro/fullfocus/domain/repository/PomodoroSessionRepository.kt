package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.data.TaskWithPomodoroSessionsDomain
import kotlinx.coroutines.flow.Flow

interface PomodoroSessionRepository {

    val taskAndPomodoroSessionsCurrent: Flow<TaskWithPomodoroSessionsDomain?>

    suspend fun startSession(state: PomodoroTimePreferences)

    suspend fun completeSession()

    suspend fun cancelSession()

    suspend fun skipSession()
}