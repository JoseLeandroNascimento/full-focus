package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.ui.form.CreateTaskForm

data class CreateTaskUiState(
    val isLoading: Boolean = true,
    val tags: List<TagDomain> = emptyList(),
    val form: CreateTaskForm = CreateTaskForm(),
    val createTagBottomSheetShow: Boolean = false
)
