package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.dao.PomodoroSessionDao
import com.joseleandro.fullfocus.data.local.database.mapper.toDomain
import com.joseleandro.fullfocus.data.local.database.model.entity.PomodoroSessionEntity
import com.joseleandro.fullfocus.data.local.database.model.enums.SessionStatus
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.data.TaskWithPomodoroSessionsDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PomodoroSessionLocalDataSourceImpl(
    private val pomodoroTimePreferences: PomodoroTimePreferenceDataSource,
    private val pomodoroSessionDao: PomodoroSessionDao
) : PomodoroSessionLocalDataSource {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val taskAndPomodoroSessionsCurrent: Flow<TaskWithPomodoroSessionsDomain?>
        get() =
            pomodoroTimePreferences.pomodoroFlow.flatMapLatest { pomodoroTimePreferences ->
                if (pomodoroTimePreferences.idTask == null) {
                    flowOf(null)
                } else {
                    pomodoroSessionDao.getTaskWithSessions(taskId = pomodoroTimePreferences.idTask)
                        .map { response ->
                            response.copy(
                                sessions = response.sessions.sortedBy { it.createdAt }
                            ).toDomain()
                        }
                }
            }

    private val pomodoroTimerCurrent = pomodoroTimePreferences.pomodoroFlow

    override suspend fun startSession(state: PomodoroTimePreferences) {


        val taskId = state.idTask ?: return

        val existing = pomodoroSessionDao.getActiveSession(taskId)
        if (existing != null) return

        val now = System.currentTimeMillis()

        val session = PomodoroSessionEntity(
            taskId = taskId,
            type = state.statusSession,
            duration = state.duration,
            status = SessionStatus.IN_PROGRESS,
            startedAt = now,
            endedAt = null
        )

        try {
            pomodoroSessionDao.create(session)
        } catch (e: Exception) {
            // já existe sessão ativa → ignora
        }
    }

    override suspend fun completeSession() =
        updateSession(SessionStatus.COMPLETED)

    override suspend fun cancelSession() =
        updateSession(SessionStatus.CANCELED)

    override suspend fun skipSession() =
        updateSession(SessionStatus.SKIPPED)

    private suspend fun updateSession(
        status: SessionStatus
    ) {
        val state = pomodoroTimerCurrent.first()
        val taskId = state.idTask ?: return

        val existing = pomodoroSessionDao.getActiveSession(taskId) ?: return

        val updated = existing.copy(
            endedAt = System.currentTimeMillis(),
            status = status
        )

        pomodoroSessionDao.update(updated)
    }

}