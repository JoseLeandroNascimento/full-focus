package com.joseleandro.fullfocus.ui.event

sealed interface PomodoroEvent {

    data class OnShowPomodoroSettingBottomSheet(val show: Boolean) : PomodoroEvent

    data object OnResetTaskCurrentPomodoro : PomodoroEvent

}