package com.joseleandro.fullfocus.ui.event

import android.content.Context
import com.joseleandro.fullfocus.ui.state.PomodoroModalTypeUiState

sealed interface PomodoroEvent {

    data object OnResetTaskCurrentPomodoro : PomodoroEvent

    data class OnSelectTask(val id: Int) : PomodoroEvent

    data class OnShowModal(val modal: PomodoroModalTypeUiState) : PomodoroEvent

    data object CloseModal : PomodoroEvent

    data class OnActionPomodoro(
        val context: Context,
        val actionEvent: PomodoroActionControlsEvent
    ) : PomodoroEvent
}