package com.joseleandro.fullfocus.domain.data

sealed interface PomodoroSessionDomain {

    data object Pomodoro

    data object PauseBreak

    data object PauseLong

}