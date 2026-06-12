package com.joseleandro.fullfocus.core.viewModel

import androidx.lifecycle.ViewModel
import com.joseleandro.fullfocus.core.model.NavigationUiState
import com.joseleandro.fullfocus.core.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FullFocusNavigation : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState: StateFlow<NavigationUiState> = _uiState.asStateFlow()

    fun onBack() {
        _uiState.update { state ->
            state.copy(
                backStack = state.backStack.dropLast(1)
            )
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