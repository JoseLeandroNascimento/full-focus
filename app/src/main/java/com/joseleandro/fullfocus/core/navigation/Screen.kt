package com.joseleandro.fullfocus.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object MainScreen : Screen
}