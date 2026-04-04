package com.joseleandro.fullfocus.ui.event

sealed interface PomodoroEvent {

    data object OnPlay : PomodoroEvent

    data object OnPause : PomodoroEvent

    data object OnRestart : PomodoroEvent

}