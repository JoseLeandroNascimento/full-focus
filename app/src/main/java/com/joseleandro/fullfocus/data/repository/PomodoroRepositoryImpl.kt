package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroDataSource
import com.joseleandro.fullfocus.data.datasource.PomodoroSettingDataSource
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
import com.joseleandro.fullfocus.domain.effect.PomodoroEffect
import com.joseleandro.fullfocus.domain.model.PomodoroDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class PomodoroRepositoryImpl(
    private val pomodoroSettingDataSource: PomodoroSettingDataSource,
    private val pomodoroDataSource: PomodoroDataSource
) : PomodoroRepository {

    private val ticker = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(1000)
        }
    }

    private val _effect = MutableSharedFlow<PomodoroEffect>()
    override val effect: SharedFlow<PomodoroEffect> = _effect.asSharedFlow()

    override val pomodoro: Flow<PomodoroDomain> = combine(
        pomodoroSettingDataSource.pomodoroSetting,
        pomodoroDataSource.session,
        pomodoroDataSource.focusCount,
        pomodoroDataSource.completedPomodoroCount,
        ticker
    ) { pomodoroSettings, session, focusCount, completedPomodoroCount, now ->
        if (session == null) {
            PomodoroDomain(
                duration = pomodoroSettings.focusTime,
                time = 0,
                isRunning = false,
                pomodoroState = PomodoroState.FOCUS,
                focusCount = focusCount,
                completedPomodoroCount = completedPomodoroCount,
                sessionsUntilLongPause = pomodoroSettings.sessionsUntilLongPause
            )
        } else {
            val isRunning = session.status == SessionStatus.RUNNING
            val elapsedTime = if (isRunning) {
                session.elapsedTime + (session.lastStartTime?.let { now - it } ?: 0L)
            } else {
                session.elapsedTime
            }

            /**
             * Verifica o fim do time do sessão de pomodoro
             */
            if (elapsedTime >= session.duration) {
                when (session.state) {
                    PomodoroState.FOCUS -> _effect.emit(PomodoroEffect.FocusFinished)
                    PomodoroState.SHORT_PAUSE -> _effect.emit(PomodoroEffect.ShortBreakFinished)
                    PomodoroState.LONG_PAUSE -> _effect.emit(PomodoroEffect.LongBreakFinished)
                }
            }

            PomodoroDomain(
                time = elapsedTime,
                duration = session.duration,
                pomodoroState = session.state,
                isRunning = isRunning,
                focusCount = focusCount,
                completedPomodoroCount = completedPomodoroCount,
                sessionsUntilLongPause = pomodoroSettings.sessionsUntilLongPause
            )
        }
    }


    override suspend fun play() {
        val setting = pomodoroSettingDataSource.pomodoroSetting.first()
        pomodoroDataSource.play(
            focusTime = setting.focusTime,
            shortPauseTime = setting.shortPauseTime,
            longPauseTime = setting.longPauseTime,
            sessionsUntilLongPause = setting.sessionsUntilLongPause
        )
    }

    override suspend fun pause() {
        pomodoroDataSource.pause()
    }

    override suspend fun reverse() {
        pomodoroDataSource.reverse()
    }

    override suspend fun cancelAndSave() {
        pomodoroDataSource.cancelAndSave()
    }

    override suspend fun cancelAndDelete() {
        pomodoroDataSource.cancelAndDelete()
    }

    override suspend fun skip() {
        val setting = pomodoroSettingDataSource.pomodoroSetting.first()
        val session = pomodoro.first()

        when (session.pomodoroState) {
            PomodoroState.FOCUS -> _effect.emit(PomodoroEffect.FocusFinished)
            PomodoroState.SHORT_PAUSE -> _effect.emit(PomodoroEffect.ShortBreakFinished)
            PomodoroState.LONG_PAUSE -> _effect.emit(PomodoroEffect.LongBreakFinished)
        }

        pomodoroDataSource.skip(
            focusTime = setting.focusTime,
            shortPauseTime = setting.shortPauseTime,
            longPauseTime = setting.longPauseTime,
            sessionsUntilLongPause = setting.sessionsUntilLongPause
        )
    }

    override suspend fun completeSession() {
        val setting = pomodoroSettingDataSource.pomodoroSetting.first()
        pomodoroDataSource.completeActiveSession(
            focusTime = setting.focusTime,
            shortPauseTime = setting.shortPauseTime,
            longPauseTime = setting.longPauseTime,
            sessionsUntilLongPause = setting.sessionsUntilLongPause
        )
    }

    companion object {

        private val TAG = "PomodoroRepository"
    }
}
