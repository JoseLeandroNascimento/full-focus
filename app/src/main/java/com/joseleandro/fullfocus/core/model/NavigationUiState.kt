package com.joseleandro.fullfocus.core.model

data class NavigationUiState(
    val backStack: List<Screen> = listOf(Screen.PomodoroScreen)
)