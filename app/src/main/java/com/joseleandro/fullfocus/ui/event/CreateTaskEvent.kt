package com.joseleandro.fullfocus.ui.event

sealed interface CreateTaskEvent {

    data class OnSave(
        val onSuccess: () -> Unit
    ) : CreateTaskEvent

    data class OnTitleChange(
        val value: String
    ) : CreateTaskEvent

    data class OnPomodorosChange(
        val value: Int
    ) : CreateTaskEvent

}