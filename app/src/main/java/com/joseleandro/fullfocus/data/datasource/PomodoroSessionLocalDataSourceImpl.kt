package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.dao.PomodoroSessionDao
import com.joseleandro.fullfocus.data.local.database.mapper.toDomain
import com.joseleandro.fullfocus.data.local.database.model.entity.PomodoroSessionEntity
import com.joseleandro.fullfocus.data.local.database.model.enums.SessionStatus
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession
import com.joseleandro.fullfocus.domain.data.TaskWithPomodoroSessionsDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class PomodoroSessionLocalDataSourceImpl(
    private val pomodoroTimePreferences: PomodoroTimePreferenceDataSource,
    private val pomodoroSessionDao: PomodoroSessionDao,
    private val taskLocalDataSource: TaskLocalDataSource
) : PomodoroSessionLocalDataSource {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val taskAndPomodoroSessionsCurrent: Flow<TaskWithPomodoroSessionsDomain?>
        get() =
            pomodoroTimePreferences.pomodoroFlow.flatMapLatest { pomodoroTimePreferences ->
                if (pomodoroTimePreferences.idTask == null) {
                    pomodoroSessionDao.getActiveSessionWithTask().map { activeSessionWithTask ->
                        if (activeSessionWithTask == null) {
                            null
                        } else {
                            TaskWithPomodoroSessionsDomain(
                                task = null,
                                sessions = listOf(activeSessionWithTask.session.toDomain())
                            )
                        }
                    }
                } else {
                    pomodoroSessionDao.getTaskWithSessions(taskId = pomodoroTimePreferences.idTask)
                        .map { response ->
                            response.copy(
                                sessions = response.sessions.sortedBy { it.createdAt }
                            ).toDomain()
                        }
                }
            }

    override suspend fun startSession(state: PomodoroTimePreferences) {


        val taskId = state.idTask

        val existing = pomodoroSessionDao.getActiveSession()
        if (existing != null) return

        val now = System.currentTimeMillis()

        val session = PomodoroSessionEntity(
            taskId = taskId,
            pomodoroId = state.pomodoroId,
            sessionId = state.sessionId,
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

    override suspend fun completeSession() {

        val existing = pomodoroSessionDao.getActiveSession() ?: return

        updateProgressTask(idTask = existing.taskId, statusSessionCurrent = existing.type)

        val updated = existing.copy(
            endedAt = System.currentTimeMillis(),
            status = SessionStatus.COMPLETED
        )

        pomodoroSessionDao.update(updated)
    }

    override suspend fun cancelSession() =
        updateStatusSession(SessionStatus.CANCELED)

    override suspend fun skipSession() {

        val existing = pomodoroSessionDao.getActiveSession() ?: return

        updateProgressTask(idTask = existing.taskId, statusSessionCurrent = existing.type)

        val updated = existing.copy(
            endedAt = System.currentTimeMillis(),
            status = SessionStatus.SKIPPED
        )

        pomodoroSessionDao.update(updated)

    }

    override suspend fun restartSession() {
        val existing = pomodoroSessionDao.getActiveSession() ?: return

        val updated = existing.copy(
            startedAt = System.currentTimeMillis()
        )
        pomodoroSessionDao.update(updated)
    }

    override suspend fun updateActiveSessionTask(taskId: Int?) {
        val existing = pomodoroSessionDao.getActiveSession() ?: return
        val updated = existing.copy(taskId = taskId)
        pomodoroSessionDao.update(updated)
    }

    private suspend fun updateStatusSession(
        status: SessionStatus
    ) {
        val existing = pomodoroSessionDao.getActiveSession() ?: return

        val updated = existing.copy(
            endedAt = System.currentTimeMillis(),
            status = status
        )

        pomodoroSessionDao.update(updated)
    }

    private suspend fun updateProgressTask(idTask: Int?, statusSessionCurrent: StatusSession) {

        val taskCurrent =
            idTask?.let { idTask -> taskLocalDataSource.getTaskById(id = idTask).first() }

        if ((statusSessionCurrent == StatusSession.PAUSE_LONG || statusSessionCurrent == StatusSession.PAUSE_SHORT) && taskCurrent != null) {
            taskLocalDataSource.setProgressPomodoro(
                id = taskCurrent.id,
                progress = taskCurrent.progress + 1
            )
        }
    }

}