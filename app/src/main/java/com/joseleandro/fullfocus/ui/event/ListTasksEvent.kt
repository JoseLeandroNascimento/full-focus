package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.state.ListTasksFilter

sealed interface ListTasksEvent {

    data object OnLoad : ListTasksEvent

    data object OnReset : ListTasksEvent

    data class OnSelectTask(val idTask: Int) : ListTasksEvent

    data class OnFilter(val filter: ListTasksFilter) : ListTasksEvent

    data class OnChangeVisibilityCreateTaskBottomSheetShow(val visible: Boolean) : ListTasksEvent

    data class OnChangeVisibilityCreateTagBottomSheetShow(val visible: Boolean) : ListTasksEvent

}