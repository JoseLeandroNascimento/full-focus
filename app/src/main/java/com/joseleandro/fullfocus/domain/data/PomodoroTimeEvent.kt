package com.joseleandro.fullfocus.domain.data

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences

sealed interface PomodoroTimeEvent {
    data class PomodoroTimeCompleted(val count: Int) : PomodoroTimeEvent
    data class Start(val sessionInfo: PomodoroTimePreferences) : PomodoroTimeEvent
    object Pause : PomodoroTimeEvent
    object Play : PomodoroTimeEvent
    object Reset : PomodoroTimeEvent
    object Skip : PomodoroTimeEvent
}