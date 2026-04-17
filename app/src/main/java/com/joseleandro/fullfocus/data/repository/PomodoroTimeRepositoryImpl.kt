package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroTimePreferenceDataSource
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.repository.PomodoroTimeRepository
import kotlinx.coroutines.flow.Flow


class PomodoroTimeRepositoryImpl(
    private val pomodoroTimePreferenceDataSource: PomodoroTimePreferenceDataSource
) : PomodoroTimeRepository {

    override val pomodoroFlow: Flow<PomodoroTimePreferences> =
        pomodoroTimePreferenceDataSource.pomodoroFlow

    override suspend fun start() =
        pomodoroTimePreferenceDataSource.start()

    override suspend fun pause() =
        pomodoroTimePreferenceDataSource.pause()


    override suspend fun play() =
        pomodoroTimePreferenceDataSource.play()

    override suspend fun reset() =
        pomodoroTimePreferenceDataSource.reset()

    override suspend fun restart() =
        pomodoroTimePreferenceDataSource.restart()

    override suspend fun skip() {
        pomodoroTimePreferenceDataSource.skip()
    }

    override suspend fun currentTask(id: Int?) {
        pomodoroTimePreferenceDataSource.currentTask(id)
    }

    override fun getRemaining(state: PomodoroTimePreferences): Long =
        pomodoroTimePreferenceDataSource.getRemaining(state = state)

    override suspend fun finishedSessionPomodoro() {
        pomodoroTimePreferenceDataSource.finishedSessionPomodoro()
    }
}
