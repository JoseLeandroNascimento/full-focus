package com.joseleandro.fullfocus.ui.screen

import androidx.lifecycle.ViewModel
import com.joseleandro.fullfocus.core.navigation.Screen
import com.joseleandro.fullfocus.core.navigation.TabScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel : ViewModel() {

    private val _backStack = MutableStateFlow<List<Screen>>(listOf(Screen.MainScreen))
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    private val _tabBackStack = MutableStateFlow<List<TabScreen>>(listOf(TabScreen.PomodoroScreen))
    val tabBackStack: StateFlow<List<TabScreen>> = _tabBackStack.asStateFlow()

    val currentTab
        get() = tabBackStack.value.last()

    val currentScreen
        get() = backStack.value.last()

    fun navigateTo(screen: Screen) {
        _backStack.value += screen
    }

    fun selectedTab(tab: TabScreen) {
        _tabBackStack.value += tab
        _tabBackStack.update {
            it.drop(0)
        }
    }

    fun onBack() {
        _backStack.value = backStack.value.dropLast(1)
    }

}