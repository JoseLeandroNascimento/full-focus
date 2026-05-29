package com.joseleandro.fullfocus.domain.effect

sealed interface PomodoroEffect {

    data object FocusFinished : PomodoroEffect

    data object ShortBreakFinished : PomodoroEffect

    data object LongBreakFinished : PomodoroEffect
}
