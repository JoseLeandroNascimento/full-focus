package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.ui.form.CreateTagForm

data class CreateTagUiState(
    val isLoading: Boolean = true,
    val form: CreateTagForm = CreateTagForm(),
)
