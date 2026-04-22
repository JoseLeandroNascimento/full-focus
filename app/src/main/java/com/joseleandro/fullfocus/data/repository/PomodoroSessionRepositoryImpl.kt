package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroSessionLocalDataSource
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.data.TaskWithPomodoroSessionsDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroSessionRepository
import kotlinx.coroutines.flow.Flow

class PomodoroSessionRepositoryImpl(
    private val pomodoroSessionLocalDataSource: PomodoroSessionLocalDataSource
) : PomodoroSessionRepository {
    override val taskAndPomodoroSessionsCurrent: Flow<TaskWithPomodoroSessionsDomain?>
        get() = pomodoroSessionLocalDataSource.taskAndPomodoroSessionsCurrent

    override suspend fun startSession(state: PomodoroTimePreferences) =
        pomodoroSessionLocalDataSource.startSession(state = state)

    override suspend fun completeSession() =
        pomodoroSessionLocalDataSource.completeSession()

    override suspend fun cancelSession() =
        pomodoroSessionLocalDataSource.cancelSession()

    override suspend fun skipSession() =
        pomodoroSessionLocalDataSource.skipSession()
}