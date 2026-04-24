package com.joseleandro.fullfocus.ui.event

sealed interface PomodoroActionControlsEvent {
    data object OnStart : PomodoroActionControlsEvent
    data object OnPause : PomodoroActionControlsEvent
    data object OnPlay : PomodoroActionControlsEvent
    data object OnReset : PomodoroActionControlsEvent
    data object OnSkip : PomodoroActionControlsEvent
    data object OnCancelCompleted : PomodoroActionControlsEvent
    data object OnCancelDiscard : PomodoroActionControlsEvent
}