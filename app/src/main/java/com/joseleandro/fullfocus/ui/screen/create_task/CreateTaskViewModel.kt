package com.joseleandro.fullfocus.ui.screen.create_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.usecase.SaveTaskUseCase
import com.joseleandro.fullfocus.ui.event.CreateTaskEvent
import com.joseleandro.fullfocus.ui.state.CreateTaskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTaskViewModel(
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: CreateTaskEvent) {

        when (event) {
            is CreateTaskEvent.OnTitleChange -> updateForm(title = event.value)
            is CreateTaskEvent.OnPomodorosChange -> updateForm(pomodoros = event.value)
            is CreateTaskEvent.OnSave -> save(
                onSuccess = event.onSuccess
            )
        }
    }

    private fun updateForm(title: String? = null, pomodoros: Int? = null) {

        _uiState.update { state ->

            state.copy(
                form = state.form.copy(
                    title = state.form.title.update(
                        value = title ?: state.form.title.value

                    ),
                    pomodoros = state.form.pomodoros.update(
                        value = pomodoros ?: state.form.pomodoros.value
                    )
                )
            )
        }

    }

    private fun save(onSuccess: () -> Unit) {

        viewModelScope.launch {


            _uiState.value.form.apply {

                if (isValid()) {
                    val title = title.value
                    val pomodoros = pomodoros.value
                    saveTaskUseCase(title = title, pomodoros = pomodoros)
                    onSuccess()
                } else {
                    _uiState.update { state ->
                        state.copy(
                            form = state.form.validateForm()
                        )
                    }
                }
            }
        }
    }
}