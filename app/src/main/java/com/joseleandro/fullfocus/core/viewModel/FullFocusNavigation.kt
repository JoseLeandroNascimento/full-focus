package com.joseleandro.fullfocus.core.viewModel

import androidx.lifecycle.ViewModel
import com.joseleandro.fullfocus.core.model.NavigationUiState
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

}