package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.model.PomodoroEntity
import com.joseleandro.fullfocus.data.local.database.model.SessionEntity
import kotlinx.coroutines.flow.Flow

interface PomodoroDataSource {

    val pomodoro: Flow<PomodoroEntity?>

    val session: Flow<SessionEntity?>

    val focusCount: Flow<Int>

    val completedPomodoroCount: Flow<Int>

    suspend fun play(focusTime: Long, shortPauseTime: Long, longPauseTime: Long, sessionsUntilLongPause: Int)

    suspend fun pause()

    suspend fun reverse()

    suspend fun cancelAndSave()

    suspend fun cancelAndDelete()

    suspend fun skip(focusTime: Long, shortPauseTime: Long, longPauseTime: Long, sessionsUntilLongPause: Int)

    suspend fun completeActiveSession(focusTime: Long, shortPauseTime: Long, longPauseTime: Long, sessionsUntilLongPause: Int)
}
