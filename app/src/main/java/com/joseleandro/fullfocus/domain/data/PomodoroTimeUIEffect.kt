package com.joseleandro.fullfocus.domain.data

import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences

sealed interface PomodoroTimeUIEffect {
    data class PomodoroTimeCompleted(val count: Int) : PomodoroTimeUIEffect
    data class Start(val sessionInfo: PomodoroTimePreferences) : PomodoroTimeUIEffect
    data class UpdateTaskId(val taskId: Int?) : PomodoroTimeUIEffect
    object Restart : PomodoroTimeUIEffect
    object Skip : PomodoroTimeUIEffect
    data class Cancel(val completed: Boolean = false) : PomodoroTimeUIEffect
}
