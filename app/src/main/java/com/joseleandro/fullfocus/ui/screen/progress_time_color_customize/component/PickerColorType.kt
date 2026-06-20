package com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.component

import androidx.annotation.StringRes
import com.joseleandro.fullfocus.R

enum class PickerColorType(@get:StringRes val label: Int) {
    FOCUS_PICKER_COLOR(label = R.string.picker_color_foco),
    SHORT_BREAK_PICKER_COLOR(label = R.string.picker_color_pausa_curta),
    LONG_BREAK_PICKER_COLOR(label = R.string.picker_color_pausa_longa)
}
