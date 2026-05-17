package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.state.PomodoroModalUiState

sealed interface PomodoroEvent {

    data object OnPlay : PomodoroEvent

    data object OnPause : PomodoroEvent

    data object OnCancel : PomodoroEvent

    data object OnRestart : PomodoroEvent

    data class OnShowModal(val modal: PomodoroModalUiState) : PomodoroEvent

    data object OnCloseModal : PomodoroEvent

}