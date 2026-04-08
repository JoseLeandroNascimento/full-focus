package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.data.TaskDomain

data class ListTasksUiState(
    val isLoading: Boolean = true,
    val tags: List<TagDomain> = emptyList(),
    val tasks: List<TaskDomain> = emptyList(),
    val filter: ListTasksFilter = ListTasksFilter(),
    val createTaskBottomSheetShow: Boolean = false,
    val createTagBottomSheetShow: Boolean = false
)

data class ListTasksFilter(
    val tag: Int? = null
)
