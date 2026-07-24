package com.joseleandro.fullfocus.core.model

import kotlinx.serialization.Serializable

interface TabScreen {

    @Serializable
    data object PomodoroTabScreen : TabScreen

    @Serializable
    data object ScoreTabScreen : TabScreen
}