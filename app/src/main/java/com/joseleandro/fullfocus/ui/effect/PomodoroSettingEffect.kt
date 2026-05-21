package com.joseleandro.fullfocus.ui.effect

sealed interface PomodoroSettingEffect {
    data object CloseBottomSheet : PomodoroSettingEffect
}