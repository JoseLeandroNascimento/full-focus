package com.joseleandro.fullfocus.ui.state

import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.component.PickerColorType
import com.joseleandro.fullfocus.ui.theme.ColorStyle

data class PickerColorUiState(
    val type: PickerColorType = PickerColorType.FOCUS_PICKER_COLOR,
    val selectedColor: ColorStyle = ColorStyle.fromColor(androidx.compose.ui.graphics.Color(0xFFEF5350)),
    val showCustomPicker: Boolean = false,
    val selectedTab: Int = 0
)
