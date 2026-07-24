package com.joseleandro.fullfocus.core.viewModel

import androidx.lifecycle.ViewModel
import com.joseleandro.fullfocus.core.model.NavigationUiState
import com.joseleandro.fullfocus.core.model.Screen
import com.joseleandro.fullfocus.core.model.TabScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FullFocusNavigationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState: StateFlow<NavigationUiState> = _uiState.asStateFlow()

    fun selectTab(tabScreen: TabScreen) {
        _uiState.update { state ->
            state.copy(
                tabSelected = tabScreen,
                tabStack = state.tabStack.filter { it != tabScreen } + tabScreen,
                backStack = listOf(Screen.MainScreen)
            )
        }
    }

    fun onBack() {
        _uiState.update { state ->
            if (state.backStack.size > 1) {
                state.copy(
                    backStack = state.backStack.dropLast(1)
                )
            } else if (state.tabStack.size > 1) {
                val newTabStack = state.tabStack.dropLast(1)
                state.copy(
                    tabStack = newTabStack,
                    tabSelected = newTabStack.last()
                )
            } else {
                state
            }
        }
    }

    fun navigate(screen: Screen) {
        _uiState.update { state ->
            state.copy(
                backStack = state.backStack + screen
            )
        }
    }

}