package com.joseleandro.fullfocus.core.navigation

import kotlinx.serialization.Serializable

sealed interface TabScreen {

    @Serializable
    data object PomodoroScreen: TabScreen


}