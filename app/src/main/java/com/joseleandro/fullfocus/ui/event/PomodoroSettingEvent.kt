package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.state.TimerType

sealed interface PomodoroSettingEvent {
    data class OnTimerClick(val type: TimerType) : PomodoroSettingEvent
    data class OnTimerConfirm(val value: Int) : PomodoroSettingEvent
    data object OnDismissTimePicker : PomodoroSettingEvent
    
    // Futuros eventos para os switches e outros inputs
    data class OnAutoStartPomodoroChange(val value: Boolean) : PomodoroSettingEvent
    data class OnAutoStartShortBreakChange(val value: Boolean) : PomodoroSettingEvent
    data class OnAutoStartLongBreakChange(val value: Boolean) : PomodoroSettingEvent
    data class OnLongBreakIntervalChange(val value: Int) : PomodoroSettingEvent
}