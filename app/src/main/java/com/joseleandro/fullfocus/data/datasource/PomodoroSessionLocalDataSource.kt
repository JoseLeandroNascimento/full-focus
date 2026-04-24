package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.data.TaskWithPomodoroSessionsDomain
import kotlinx.coroutines.flow.Flow

interface PomodoroSessionLocalDataSource {

    val taskAndPomodoroSessionsCurrent: Flow<TaskWithPomodoroSessionsDomain?>

    suspend fun startSession(state: PomodoroTimePreferences)

    suspend fun completeSession()

    suspend fun cancelSession()

    suspend fun skipSession()

    suspend fun restartSession()

    suspend fun updateActiveSessionTask(taskId: Int?)
}
