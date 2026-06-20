package com.joseleandro.fullfocus.ui.event

import com.joseleandro.fullfocus.ui.theme.ColorStyle

sealed interface PickerColorEvent {
    data class OnColorSelected(val color: ColorStyle) : PickerColorEvent
    data class OnTabChanged(val tabIndex: Int) : PickerColorEvent
    data class OnToggleCustomPicker(val show: Boolean) : PickerColorEvent
    data object OnConfirm : PickerColorEvent
    data object OnCancel : PickerColorEvent
}
