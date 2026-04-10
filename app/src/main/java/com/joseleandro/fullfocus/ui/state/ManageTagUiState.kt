package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.domain.data.TagWithTasksDetailsDomain

data class ManageTagUiState(
    val isLoading: Boolean = true,
    val tags: List<TagWithTasksDetailsDomain> = emptyList(),
    val createTagBottomSheetShow: Boolean = false
)
