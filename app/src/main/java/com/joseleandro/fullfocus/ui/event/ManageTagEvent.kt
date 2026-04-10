package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.domain.data.TagWithTasksDetailsDomain

sealed interface ManageTagEvent {

    data object OnLoad : ManageTagEvent

    data class OnChangeVisibilityCreateTagBottomSheetShow(val visibility: Boolean) : ManageTagEvent

    data class OnDelete(val tag: TagWithTasksDetailsDomain) : ManageTagEvent


}