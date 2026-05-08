package com.joseleandro.fullfocus.core.model

import kotlinx.serialization.Serializable


sealed interface Screen {

    @Serializable
    data object PomodoroScreen : Screen

}