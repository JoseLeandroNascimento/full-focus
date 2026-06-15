package com.joseleandro.fullfocus.ui.screen.config_sound

import androidx.annotation.StringRes
import com.joseleandro.fullfocus.R

enum class TabConfigSound(@get:StringRes val labelRes: Int, val index: Int) {
    FOCUS_OPTIONS(labelRes = R.string.foco_option, index = 0),
    BREAK_OPTIONS(labelRes = R.string.pausa_option, index = 1)
}