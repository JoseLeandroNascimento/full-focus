package com.joseleandro.fullfocus.ui.event

sealed interface CreateTaskEvent {

    data object OnLoad : CreateTaskEvent

    data class OnSave(
        val onSuccess: () -> Unit
    ) : CreateTaskEvent

    data class OnTitleChange(
        val value: String
    ) : CreateTaskEvent

    data class OnPomodorosChange(
        val value: Int
    ) : CreateTaskEvent

    data object OnReset : CreateTaskEvent

    data class OnTagToggle(val idTag: Int) : CreateTaskEvent

    data class OnChangeVisibilityCreateTagBottomSheetShow(val visible: Boolean) : CreateTaskEvent

}