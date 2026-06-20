package com.joseleandro.fullfocus.domain.effect

import com.joseleandro.fullfocus.ui.theme.ColorStyle

sealed interface PickerColorEffect {
    data class ConfirmColor(val color: ColorStyle) : PickerColorEffect
    data object NavigateBack : PickerColorEffect
}
