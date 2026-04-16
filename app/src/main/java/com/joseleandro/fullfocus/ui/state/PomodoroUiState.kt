package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroStatus
import com.joseleandro.fullfocus.data.local.preferences.data.StatusSession


data class PomodoroUiState(
    val time: Int = 0,
    val timeSession: Int = 0,
    val isPlay: Boolean = false,
    val showPomodoroSettingBottomSheet: Boolean = false,
    val pomodoroStatus: PomodoroStatus = PomodoroStatus.START,
    val statusSession: StatusSession = StatusSession.FOCUS,
    val currentSession: Int = 0
)
