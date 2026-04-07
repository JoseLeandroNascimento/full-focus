package com.joseleandro.fullfocus.ui.screen.list_tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.usecase.TagFindAllUseCase
import com.joseleandro.fullfocus.ui.event.ListTasksEvent
import com.joseleandro.fullfocus.ui.state.ListTasksFilter
import com.joseleandro.fullfocus.ui.state.ListTasksUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListTasksViewModel(
    private val tagFindAllUseCase: TagFindAllUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListTasksUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ListTasksEvent) {
        when (event) {
            ListTasksEvent.OnLoad -> load()
            ListTasksEvent.OnReset -> reset()
            is ListTasksEvent.OnChangeVisibilityCreateTagBottomSheetShow -> changeVisibilityCreateTagBottomSheetShow(
                event.visible
            )

            is ListTasksEvent.OnChangeVisibilityCreateTaskBottomSheetShow -> changeVisibilityCreateTaskBottomSheetShow(
                event.visible
            )

            is ListTasksEvent.OnFilter -> filter(event.filter)
        }

    }

    private fun load() {

        changeVisibilityLoading(isLoading = true)

        viewModelScope.launch {

            tagFindAllUseCase().collect { tags ->
                _uiState.update { state ->
                    state.copy(
                        tags = tags
                    )
                }
                changeVisibilityLoading(isLoading = false)
            }
        }
    }

    private fun filter(value: ListTasksFilter) {

        _uiState.update { state ->
            state.copy(
                filter = value
            )
        }
    }

    private fun changeVisibilityCreateTagBottomSheetShow(visible: Boolean) {
        _uiState.update { state ->
            state.copy(
                createTagBottomSheetShow = visible
            )
        }

    }

    private fun changeVisibilityCreateTaskBottomSheetShow(visible: Boolean) {
        _uiState.update { state ->
            state.copy(
                createTaskBottomSheetShow = visible
            )
        }
    }

    private fun changeVisibilityLoading(isLoading: Boolean) {
        _uiState.update { state ->
            state.copy(
                isLoading = isLoading
            )
        }
    }

    private fun reset() {
        _uiState.value = ListTasksUiState()
    }

}