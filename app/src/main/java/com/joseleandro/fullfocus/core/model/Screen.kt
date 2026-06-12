package com.joseleandro.fullfocus.core.model

import kotlinx.serialization.Serializable


sealed interface Screen {

    @Serializable
    data object PomodoroScreen : Screen

    data class SittingSoundPomodoroScreen(val type: SettingSound) : Screen

}