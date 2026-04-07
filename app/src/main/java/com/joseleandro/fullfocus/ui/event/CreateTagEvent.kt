package com.joseleandro.fullfocus.ui.event

import androidx.compose.ui.graphics.Color

sealed interface CreateTagEvent {

    data class OnLoad(val id: Int?) : CreateTagEvent

    data object OnReset : CreateTagEvent

    data class OnNameChange(val name: String) : CreateTagEvent

    data class OnColorChange(val color: Color) : CreateTagEvent

    data class OnSave(val onSuccess: (() -> Unit)? = null) : CreateTagEvent

    data class OnChangeVisibilityLoading(val isLoading: Boolean) : CreateTagEvent

}