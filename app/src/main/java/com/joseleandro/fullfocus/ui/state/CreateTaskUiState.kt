package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.ui.form.CreateTaskForm

data class CreateTaskUiState(
    val isLoading: Boolean = true,
    val form: CreateTaskForm = CreateTaskForm()
)
