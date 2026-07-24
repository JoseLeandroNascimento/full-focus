package com.joseleandro.fullfocus.core.model

data class NavigationUiState(
    val backStack: List<Screen> = listOf(Screen.MainScreen),
    val tabStack: List<TabScreen> = listOf(TabScreen.PomodoroTabScreen),
    val tabSelected: TabScreen = TabScreen.PomodoroTabScreen
)