package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroCurrentPreferenceDataSource
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroCurrentPreferences
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow


class PomodoroCurrentRepositoryImpl(
    private val pomodoroCurrentPreferenceDataSource: PomodoroCurrentPreferenceDataSource
) : PomodoroRepository {

    override val pomodoroFlow: Flow<PomodoroCurrentPreferences> =
        pomodoroCurrentPreferenceDataSource.pomodoroFlow

    override suspend fun start(duration: Long) =
        pomodoroCurrentPreferenceDataSource.start(duration = duration)

    override suspend fun pause() =
        pomodoroCurrentPreferenceDataSource.pause()


    override suspend fun play() =
        pomodoroCurrentPreferenceDataSource.play()

    override suspend fun resume() =
        pomodoroCurrentPreferenceDataSource.resume()

    override suspend fun reset() =
        pomodoroCurrentPreferenceDataSource.reset()


    override fun getRemaining(state: PomodoroCurrentPreferences): Long =
        pomodoroCurrentPreferenceDataSource.getRemaining(state = state)
}
