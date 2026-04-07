package com.joseleandro.fullfocus.ui.screen.create_tag

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.usecase.SaveTagUseCase
import com.joseleandro.fullfocus.ui.event.CreateTagEvent
import com.joseleandro.fullfocus.ui.form.CreateTagForm
import com.joseleandro.fullfocus.ui.state.CreateTagUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTagViewModel(
    private val saveTagUseCase: SaveTagUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTagUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: CreateTagEvent) {
        when (event) {
            is CreateTagEvent.OnColorChange -> updateForm(color = event.color)
            is CreateTagEvent.OnNameChange -> updateForm(name = event.name)
            is CreateTagEvent.OnChangeVisibilityLoading -> changeVisibilityLoading(event.isLoading)
            is CreateTagEvent.OnLoad -> load(id = event.id)
            is CreateTagEvent.OnSave -> save(onSuccess = event.onSuccess)
            CreateTagEvent.OnReset -> reset()
        }
    }

    private fun load(id: Int?) {

        viewModelScope.launch {
            // Adicionar o load de uma tag depois

            changeVisibilityLoading(false)
        }
    }

    private fun reset() {
        _uiState.value = CreateTagUiState()
    }

    private fun updateForm(id: Int? = null, name: String? = null, color: Color? = null) {

        val currentForm = _uiState.value.form

        return _uiState.update { state ->
            state.copy(
                form = CreateTagForm(
                    id = state.form.id.update(id ?: currentForm.id.value),
                    name = state.form.name.update(name ?: currentForm.name.value),
                    color = state.form.color.update(color ?: currentForm.color.value)
                )
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

    private fun save(onSuccess: (() -> Unit)? = null) {

        val formValid = _uiState.value.form.isValid()

        changeVisibilityLoading(
            isLoading = true
        )

        viewModelScope.launch {

            if (formValid) {

                _uiState.value.form.apply {
                    saveTagUseCase(
                        id = id.value,
                        name = name.value,
                        color = color.value
                    )
                }
                onSuccess?.invoke()
            } else {

                _uiState.update { state ->

                    state.copy(
                        form = state.form.validateForm()
                    )
                }

            }

            changeVisibilityLoading(
                isLoading = false
            )
        }

    }

    companion object {
        private const val TAG = "CreateTagViewModel"
    }
}