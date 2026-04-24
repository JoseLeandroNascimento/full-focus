package com.joseleandro.fullfocus.ui.screen.pomodoro

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.repository.PomodoroTimeRepository
import com.joseleandro.fullfocus.domain.usecase.GetFilteredTasksUseCase
import com.joseleandro.fullfocus.domain.usecase.GetTaskEndPomodoroSessionUseCase
import com.joseleandro.fullfocus.domain.usecase.SetCurrentTaskPomodoroUseCase
import com.joseleandro.fullfocus.service.ACTION_CANCEL_COMPLETED
import com.joseleandro.fullfocus.service.ACTION_CANCEL_DISCARD
import com.joseleandro.fullfocus.service.ACTION_PAUSE
import com.joseleandro.fullfocus.service.ACTION_PLAY
import com.joseleandro.fullfocus.service.ACTION_RESTART
import com.joseleandro.fullfocus.service.ACTION_SKIP
import com.joseleandro.fullfocus.service.ACTION_START
import com.joseleandro.fullfocus.service.PomodoroService
import com.joseleandro.fullfocus.ui.event.PomodoroActionControlsEvent
import com.joseleandro.fullfocus.ui.event.PomodoroEvent
import com.joseleandro.fullfocus.ui.state.PomodoroModalTypeUiState
import com.joseleandro.fullfocus.ui.state.PomodoroUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val repository: PomodoroTimeRepository,
    private val getTaskEndPomodoroSessionUseCase: GetTaskEndPomodoroSessionUseCase,
    private val setCurrentTaskPomodoroUseCase: SetCurrentTaskPomodoroUseCase,
    private val getFilteredTasksUseCase: GetFilteredTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            combine(
                getTaskEndPomodoroSessionUseCase(),
                repository.pomodoroFlow,
                getFilteredTasksUseCase()
            ) { taskSession, pomodoro, tasks ->
                PomodoroUiState(
                    taskCurrent = taskSession?.task,
                    timeSession = (pomodoro.duration / 1000).toInt(),
                    isPlay = pomodoro.isRunning,
                    statusSession = pomodoro.statusSession,
                    pomodoroStatus = pomodoro.pomodoroStatus,
                    currentSession = pomodoro.counterPomodoro + 1,
                    tasks = tasks,
                    time = _uiState.value.time
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
        startTicker()
    }

    private fun startTicker() {
        viewModelScope.launch {
            while (true) {
                delay(1000)

                val state = repository.pomodoroFlow.first()
                val remaining = repository.getRemaining(state) / 1000

                _uiState.update {
                    it.copy(time = remaining.toInt())
                }
            }
        }
    }

    private fun resetTaskCurrentPomodoro() {
        viewModelScope.launch {
            setCurrentTaskPomodoroUseCase(id = null)
        }
    }

    private fun changeVisibilityModal(visibility: PomodoroModalTypeUiState) {
        _uiState.update { state ->
            state.copy(
                activeModal = visibility
            )
        }
    }

    private fun setTaskCurrent(idTask: Int?) {
        viewModelScope.launch {
            setCurrentTaskPomodoroUseCase(id = idTask)
        }
    }

    fun onEvent(event: PomodoroEvent) {
        when (event) {

            PomodoroEvent.OnResetTaskCurrentPomodoro -> resetTaskCurrentPomodoro()

            is PomodoroEvent.OnSelectTask -> setTaskCurrent(idTask = event.id)

            is PomodoroEvent.OnShowModal -> changeVisibilityModal(visibility = event.modal)

            PomodoroEvent.CloseModal -> changeVisibilityModal(visibility = PomodoroModalTypeUiState.None)

            is PomodoroEvent.OnActionPomodoro -> actionControlPomodoro(
                context = event.context,
                action = event.actionEvent
            )
        }
    }

    private fun actionControlPomodoro(context: Context, action: PomodoroActionControlsEvent) {
        when (action) {
            PomodoroActionControlsEvent.OnCancelCompleted -> context.startPomodoroService(
                ACTION_CANCEL_COMPLETED
            )

            PomodoroActionControlsEvent.OnCancelDiscard -> context.startPomodoroService(
                ACTION_CANCEL_DISCARD
            )

            PomodoroActionControlsEvent.OnPause -> context.startPomodoroService(ACTION_PAUSE)
            PomodoroActionControlsEvent.OnPlay -> context.startPomodoroService(ACTION_PLAY)
            PomodoroActionControlsEvent.OnReset -> context.startPomodoroService(ACTION_RESTART)
            PomodoroActionControlsEvent.OnSkip -> context.startPomodoroService(ACTION_SKIP)
            PomodoroActionControlsEvent.OnStart -> context.startPomodoroService(ACTION_START)
        }
    }

    fun Context.startPomodoroService(action: String) {
        val intent = Intent(this, PomodoroService::class.java).apply {
            this.action = action
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

}

