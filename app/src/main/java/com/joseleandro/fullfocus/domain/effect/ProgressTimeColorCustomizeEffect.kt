package com.joseleandro.fullfocus.domain.effect

import com.joseleandro.fullfocus.ui.theme.ColorStyle

sealed interface ProgressTimeColorCustomizeEffect {
    data class ConfirmColor(val color: ColorStyle) : ProgressTimeColorCustomizeEffect
    data object NavigateBack : ProgressTimeColorCustomizeEffect
}
