package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.domain.enums.SessionStatus

private const val TIME_SESSION = 60 * 25

data class PomodoroUiState(
    val time: Int = TIME_SESSION,
    val timeSession: Int = TIME_SESSION,
    val isPlay: Boolean = false,
    val showPomodoroSettingBottomSheet: Boolean = false,
    val sessionStatus: SessionStatus = SessionStatus.START
)
