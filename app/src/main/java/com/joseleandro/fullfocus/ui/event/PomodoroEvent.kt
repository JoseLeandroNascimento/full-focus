package com.joseleandro.fullfocus.ui.event

sealed interface PomodoroEvent {

    data class OnShowPomodoroSettingBottomSheet(val show: Boolean) : PomodoroEvent

    data object OnResetTaskCurrentPomodoro : PomodoroEvent

    data class OnShowSelectTaskBottomSheet(val show: Boolean) : PomodoroEvent

    data class OnSelectTask(val id: Int) : PomodoroEvent

}