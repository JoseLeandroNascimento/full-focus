package com.joseleandro.fullfocus.ui.screen.event

sealed interface PomodoroEvent {

    data object OnPlay : PomodoroEvent

    data object OnPause : PomodoroEvent

}