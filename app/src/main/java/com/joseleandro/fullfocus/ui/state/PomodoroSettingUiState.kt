package com.joseleandro.fullfocus.ui.state

enum class TimerType {
    POMODORO, SHORT_BREAK, LONG_BREAK, LONG_BREAK_INTERVAL
}

data class PomodoroSettingUiState(
    val pomodoroTime: Int = 0,
    val shortBreakTime: Int = 0,
    val longBreakTime: Int = 0,
    val longBreakInterval: Int = 0,
    val autoStartNextPomodoro: Boolean = false,
    val autoStartNextShortBreak: Boolean = false,
    val autoStartNextLongBreak: Boolean = false,
    
    // Controle do Seletor
    val showTimePicker: Boolean = false,
    val selectedTimerType: TimerType? = null,
    val tempSelectedTime: Int = 0
)