package com.joseleandro.fullfocus.domain.effect

sealed interface PomodoroSettingEffect {
    data object CloseBottomSheet : PomodoroSettingEffect
}