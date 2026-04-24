package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroStatus
import com.joseleandro.fullfocus.data.local.preferences.data.enums.StatusSession
import com.joseleandro.fullfocus.domain.data.TaskDomain


data class PomodoroUiState(
    val time: Int = 0,
    val timeSession: Int = 0,
    val pomodoroStatus: PomodoroStatus = PomodoroStatus.START,
    val statusSession: StatusSession = StatusSession.FOCUS,
    val currentSession: Int = 0,
    val taskCurrent: TaskDomain? = null,
    val isPlay: Boolean = false,
    val tasks: List<TaskDomain> = emptyList(),
    val activeModal: PomodoroModalTypeUiState = PomodoroModalTypeUiState.None,
)
