package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.dao.PomodoroDao
import com.joseleandro.fullfocus.data.local.database.dao.SessionDao
import com.joseleandro.fullfocus.data.local.database.model.PomodoroEntity
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.SessionEntity
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class PomodoroDataSourceImpl(
    private val pomodoroDao: PomodoroDao,
    private val sessionDao: SessionDao
) : PomodoroDataSource {

    override val pomodoro: Flow<PomodoroEntity?>
        get() = pomodoroDao.getPomodoroActive()

    override val session: Flow<SessionEntity?>
        get() = sessionDao.getSessionCurrent()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val focusCount: Flow<Int>
        get() = pomodoroDao.getPomodoroActive().flatMapLatest { pomodoro ->
            if (pomodoro == null) flowOf(0)
            else sessionDao.getFocusCountByPomodoroIdFlow(pomodoro.id)
        }

    override suspend fun play(focusTime: Long, shortPauseTime: Long, longPauseTime: Long, sessionsUntilLongPause: Int) {
        val currentPomodoro = pomodoroDao.getPomodoroActive().first()
        val pomodoroId = currentPomodoro?.id ?: pomodoroDao.save(PomodoroEntity())

        val currentSession = sessionDao.getSessionCurrent().first()
        if (currentSession == null) {
            startNextSession(pomodoroId, focusTime, shortPauseTime, longPauseTime, sessionsUntilLongPause, startRunning = true)
        } else {
            sessionDao.update(
                currentSession.copy(
                    status = SessionStatus.RUNNING,
                    lastStartTime = System.currentTimeMillis()
                )
            )
        }
    }

    private suspend fun startNextSession(
        pomodoroId: Long,
        focusTime: Long,
        shortPauseTime: Long,
        longPauseTime: Long,
        sessionsUntilLongPause: Int,
        startRunning: Boolean = false
    ) {
        val lastSession = sessionDao.getLastSessionByPomodoroId(pomodoroId)
        val nextState = when (lastSession?.state) {
            PomodoroState.FOCUS -> {
                val focusCount = sessionDao.getFocusCountByPomodoroId(pomodoroId)
                if (focusCount % sessionsUntilLongPause == 0 && focusCount != 0) PomodoroState.LONG_PAUSE else PomodoroState.SHORT_PAUSE
            }

            PomodoroState.SHORT_PAUSE, PomodoroState.LONG_PAUSE -> PomodoroState.FOCUS
            null -> PomodoroState.FOCUS
        }

        val duration = when (nextState) {
            PomodoroState.FOCUS -> focusTime
            PomodoroState.SHORT_PAUSE -> shortPauseTime
            PomodoroState.LONG_PAUSE -> longPauseTime
        }

        sessionDao.save(
            SessionEntity(
                pomodoroId = pomodoroId,
                duration = duration,
                state = nextState,
                status = if (startRunning) SessionStatus.RUNNING else SessionStatus.PAUSE,
                lastStartTime = if (startRunning) System.currentTimeMillis() else null
            )
        )
    }

    override suspend fun pause() {
        val currentSession = sessionDao.getSessionCurrent().first() ?: return
        val now = System.currentTimeMillis()
        val additionalElapsed = currentSession.lastStartTime?.let { now - it } ?: 0L
        sessionDao.update(
            currentSession.copy(
                status = SessionStatus.PAUSE,
                elapsedTime = currentSession.elapsedTime + additionalElapsed,
                lastStartTime = null
            )
        )
    }

    override suspend fun reverse() {
        val currentSession = sessionDao.getSessionCurrent().first() ?: return
        sessionDao.update(
            currentSession.copy(
                status = SessionStatus.PAUSE,
                elapsedTime = 0,
                lastStartTime = null
            )
        )
    }

    override suspend fun cancelAndSave() {
        val currentPomodoro = pomodoroDao.getPomodoroActive().first() ?: return
        val currentSession = sessionDao.getSessionCurrent().first()
        if (currentSession != null) {
            sessionDao.update(
                currentSession.copy(
                    status = SessionStatus.CANCEL,
                    lastStartTime = null
                )
            )
        }
        pomodoroDao.update(currentPomodoro.copy(completed = true))
    }

    override suspend fun cancelAndDelete() {
        val currentPomodoro = pomodoroDao.getPomodoroActive().first() ?: return
        pomodoroDao.deleteById(currentPomodoro.id)
    }

    override suspend fun skip(focusTime: Long, shortPauseTime: Long, longPauseTime: Long, sessionsUntilLongPause: Int) {
        val currentSession = sessionDao.getSessionCurrent().first() ?: return
        sessionDao.update(
            currentSession.copy(
                status = SessionStatus.SKIPPED,
                lastStartTime = null
            )
        )
        startNextSession(currentSession.pomodoroId, focusTime, shortPauseTime, longPauseTime, sessionsUntilLongPause, startRunning = false)
    }

    override suspend fun completeActiveSession(
        focusTime: Long,
        shortPauseTime: Long,
        longPauseTime: Long,
        sessionsUntilLongPause: Int
    ) {
        val currentSession = sessionDao.getSessionCurrent().first() ?: return
        sessionDao.update(
            currentSession.copy(
                status = SessionStatus.COMPLETED,
                elapsedTime = currentSession.duration,
                lastStartTime = null
            )
        )

        if (currentSession.state == PomodoroState.LONG_PAUSE) {
            val currentPomodoro = pomodoroDao.getPomodoroActive().first()
            if (currentPomodoro != null) {
                pomodoroDao.update(currentPomodoro.copy(completed = true))
            }
        } else {
            startNextSession(currentSession.pomodoroId, focusTime, shortPauseTime, longPauseTime, sessionsUntilLongPause, startRunning = false)
        }
    }
}
