package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.state.PomodoroModalUiState

sealed interface PomodoroEvent {

    data class ShowModal(val modal: PomodoroModalUiState) : PomodoroEvent

    data object CloseModal : PomodoroEvent

    data object Play : PomodoroEvent

    data object Pause : PomodoroEvent

    data object Reverse : PomodoroEvent

    data object CancelAndSave : PomodoroEvent

    data object CancelAndDelete : PomodoroEvent

    data object Skip : PomodoroEvent

    data object CompleteSession : PomodoroEvent
}
