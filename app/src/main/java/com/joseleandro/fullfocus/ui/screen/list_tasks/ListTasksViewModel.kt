package com.joseleandro.fullfocus.ui.screen.list_tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.usecase.FilterTaskUseCase
import com.joseleandro.fullfocus.domain.usecase.GetArgumentFiltersTasks
import com.joseleandro.fullfocus.domain.usecase.GetFilteredTasksUseCase
import com.joseleandro.fullfocus.domain.usecase.GetTagFindAllUseCase
import com.joseleandro.fullfocus.ui.event.ListTasksEvent
import com.joseleandro.fullfocus.ui.state.ListTasksFilter
import com.joseleandro.fullfocus.ui.state.ListTasksUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListTasksViewModel(
    private val getTagFindAllUseCase: GetTagFindAllUseCase,
    private val getFilteredTasksUseCase: GetFilteredTasksUseCase,
    private val filterTaskUseCase: FilterTaskUseCase,
    private val getArgumentFiltersTasks: GetArgumentFiltersTasks,
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

            combine(
                getTagFindAllUseCase(),
                getFilteredTasksUseCase(),
                getArgumentFiltersTasks()
            ) { tags, tasks, argumentFilter ->
                Triple(tags, tasks, argumentFilter)
            }.collect { (tags, tasks, argumentFilter) ->
                _uiState.update { state ->
                    state.copy(
                        tags = tags,
                        tasks = tasks,
                        filter = ListTasksFilter(
                            tag = argumentFilter.tagFilter
                        )
                    )
                }

                Log.d("tag filter", argumentFilter.tagFilter.toString())
                changeVisibilityLoading(isLoading = false)
            }
        }
    }

    private fun filter(value: ListTasksFilter) {

        viewModelScope.launch {
            filterTaskUseCase(tagId = value.tag)
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