package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroTimePreferenceDataSource
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow


class PomodoroCurrentRepositoryImpl(
    private val pomodoroTimePreferenceDataSource: PomodoroTimePreferenceDataSource
) : PomodoroRepository {

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

    override suspend fun skip() {
        pomodoroTimePreferenceDataSource.skip()
    }

    override fun getRemaining(state: PomodoroTimePreferences): Long =
        pomodoroTimePreferenceDataSource.getRemaining(state = state)

    override suspend fun finishedSessionPomodoro() {
        pomodoroTimePreferenceDataSource.finishedSessionPomodoro()
    }
}
