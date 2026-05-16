package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.PomodoroTimerDataSource
import com.joseleandro.fullfocus.domain.model.PomodoroTimerDomain
import com.joseleandro.fullfocus.domain.repository.PomodoroTimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PomodoroTimerRepositoryImpl(
    private val pomodoroTimerDataSource: PomodoroTimerDataSource
) : PomodoroTimerRepository {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null

    private val _time = MutableStateFlow(0L)

    override val pomodoroTimer: Flow<PomodoroTimerDomain>
        get() = combine(
            pomodoroTimerDataSource.pomodoroTimer,
            _time
        ) { pomodoroTimer, time ->

            PomodoroTimerDomain(
                duration = pomodoroTimer.duration,
                isRunning = pomodoroTimer.isRunning,
                time = time,
                progress = pomodoroTimer.progress,
                pomodoroState = pomodoroTimer.pomodoroState
            )
        }

    init {
        scope.launch {
            updateTimer()
            pomodoroTimerDataSource.pomodoroTimer.collect { pomodoroTimer ->
                if (pomodoroTimer.isRunning) {
                    startTimer()
                } else {
                    stopTimer()
                }
            }
        }
    }

    override suspend fun play() {
        pomodoroTimerDataSource.start()
    }

    override suspend fun pause() {
        pomodoroTimerDataSource.pause()
    }

    override suspend fun cancel() {
       pomodoroTimerDataSource.cancel()
    }

    override suspend fun restart() {
        pomodoroTimerDataSource.restart()
    }

    override suspend fun updateTimer() {
        _time.value = pomodoroTimerDataSource.getTime()
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return

        timerJob = scope.launch {
            while (true) {
                updateTimer()
                delay(1000)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        scope.launch {
            updateTimer()
        }
    }

}
