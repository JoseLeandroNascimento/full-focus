package com.joseleandro.fullfocus.ui.screen.manage_tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.data.TagWithTasksDetailsDomain
import com.joseleandro.fullfocus.domain.usecase.DeleteTagUseCase
import com.joseleandro.fullfocus.domain.usecase.GetTagsWithDetailsUseCase
import com.joseleandro.fullfocus.ui.event.ManageTagEvent
import com.joseleandro.fullfocus.ui.state.ManageTagUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageTagViewModel(
    private val getTagsWithDetailsUseCase: GetTagsWithDetailsUseCase,
    private val deleteTagUseCase: DeleteTagUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageTagUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ManageTagEvent) {

        when (event) {
            is ManageTagEvent.OnChangeVisibilityCreateTagBottomSheetShow -> changeVisibilityCreateTagBottomSheetShow(
                event.visibility
            )

            is ManageTagEvent.OnDelete -> delete(tag = event.tag)

            ManageTagEvent.OnLoad -> load()
        }
    }

    private fun load() {

        viewModelScope.launch {
            getTagsWithDetailsUseCase().collect { response ->
                _uiState.update { state ->
                    state.copy(
                        tags = response,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun delete(tag: TagWithTasksDetailsDomain) {
        viewModelScope.launch {
            deleteTagUseCase(tag.id)
        }
    }

    private fun changeVisibilityCreateTagBottomSheetShow(visibility: Boolean) {
        _uiState.update { state ->
            state.copy(
                createTagBottomSheetShow = visibility
            )
        }
    }
}