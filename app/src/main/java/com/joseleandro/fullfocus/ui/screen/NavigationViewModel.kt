package com.joseleandro.fullfocus.ui.screen

import androidx.lifecycle.ViewModel
import com.joseleandro.fullfocus.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {

    private val _backStack = MutableStateFlow<List<Screen>>(listOf(Screen.PomodoroScreen))
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    val currentScreen
        get() = backStack.value.last()

    fun navigateTo(screen: Screen) {
        _backStack.value += screen
    }

    fun onBack() {
        _backStack.value = backStack.value.dropLast(1)
    }

}